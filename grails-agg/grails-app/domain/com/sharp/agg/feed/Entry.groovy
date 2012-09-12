package com.sharp.agg.feed

class Entry {
    String title
    String contents
    String link
    Date postedOn
    Date dateCreated

    static searchable = {
        root true
        categories component: true
        feed component: true
        spellCheck "include"
    }

    static belongsTo = [feed: Feed]
    static hasMany = [categories: Category, entryViews: EntryView]

    SortedSet categories

    static constraints = {
        title(blank: false, maxSize: 250)
        link(blank: false, url: true, unique: true, maxSize: 1000)
        contents(blank: false, maxSize: 2000)
        postedOn()
    }

    def getViewCount() {
        return entryViews.size()
    }
}
