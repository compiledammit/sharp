package com.sharp.agg.feed

import grails.plugins.springsecurity.Secured

@Secured("ROLE_ADMIN")
class EntryController {
    EntryService entryService
    FeedService feedService
    def searchableService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def search() {
        def query = params.q
        if (query) {
            params.max = Math.min(params.max as Integer ?: 10, 100)
            def opts = [suggestQuery: false]
            try{
                def results = searchableService.search(query, opts+params)
                render(view: "searchResults", model: [entries: results.results, entriesTotal: results.total, suggest: results?.suggestedQuery])
            }
            catch(Exception e){
                flash.message = message(code: 'default.search.error', default:'Ooops, something went wrong.  Please try another query.')
                redirect(controller: 'home', action: 'index')
                return
            }
        }
        else {
            redirect(controller: "home", action: "index")
            return
        }
    }

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def go(String id) {
        def entry = Entry.findById(id)

        def clientAddr = request.getRemoteAddr()
        def entryView = new EntryView()
        entryView.remoteAddress = clientAddr
        entryView.entry = entry
        entryView.save()

        redirect(url: entry.link)
    }

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def byFeed(String id) {
        if (!id) {
            redirect(controller: "home", action: "index")
            return
        }
        def feed = Feed.findById(id)
        if (!feed) {
            redirect(controller: "home", action: "index")
            return
        }
        return entryService.findByFeed(feed, params)
    }

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def byCategory(String id) {
        if (!id) {
            redirect(controller: "home", action: "index")
            return
        }
        def cat = Category.findById(id)
        if (!cat) {
            redirect(controller: "home", action: "index")
            return
        }
        return entryService.findByCategory(cat, params)
    }

    def list(Integer max) {
        def l = entryService.list(Entry, params)
        return [entryInstanceList: l.list, entryInstanceTotal: l.total]
    }

    def create() {
        def entry = entryService.create(Entry, params)
        return [entryInstance: entry.instance]
    }

    def save() {
        def entryInstance = new Entry(params);
        def saved = entryService.save(entryInstance)
        if (saved) {
            flash.message = message(code: 'default.created.message', args: [message(code: 'entry.label', default: 'Entry'), entryInstance.id])
            redirect(action: "show", id: entryInstance.id)
        }
        else {
            render(view: "create", model: [entryInstance: entryInstance])
        }
    }

    def show(Long id) {
        def entryInstance = Entry.findById(id)
        if (!entryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), id])
            redirect(action: "list")
            return
        }
        return [entryInstance: entryInstance]
    }

    def edit(Long id) {
        show(id)
    }

    def update(Long id, Long version) {

        def entryInstance = Entry.findById(id)

        if (!entryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (entryInstance.version > version) {
                entryInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'entry.label', default: 'Entry')] as Object[],
                        "Another user has updated this ${className} while you were editing")
            }
        }

        entryInstance.properties = params
        def saved = entryService.update(entryInstance)

        if (!entryInstance.hasErrors()) {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'entry.label', default: 'Entry'), entryInstance.id])
            redirect(action: "show", id: entryInstance.id)
        }
        else {
            render(view: "edit", model: [entryInstance: entryInstance])
            return
        }
    }

    def delete(Long id) {
        def entryInstance = Entry.findById(id)
        if (!entryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), id])
            redirect(action: "list")
            return
        }
        def deleted = entryService.delete(entryInstance)
        if (!entryInstance.hasErrors()) {
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'entry.label', default: 'Entry'), id])
            redirect(action: "list")
        }
        else {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'entry.label', default: 'Entry'), id])
            redirect(action: "show", id: id)
        }
    }
}
