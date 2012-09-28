package com.sharp.agg.feed

import org.junit.After
import org.junit.Before
import org.junit.Test

class EntryControllerTests extends BaseControllerTestCase {

    def entryService
    def feedService
    def controller
    def searchableService
    def renderMap

    @Before
    void setUp() {
        super.setUp()
        controller = new EntryController()
        controller.entryService = entryService
        controller.feedService = feedService
        controller.searchableService = searchableService

        EntryController.metaClass.render = { Map map ->
            renderMap = map
        }
    }

    @After
    void tearDown() {
        super.tearDown()
    }

    @Test
    void testIndex() {
        controller.index()

        assertEquals("/entry/list", controller.response.redirectedUrl)
    }

    private createFeed() {
        def feed = Feed.newInstance([title: 'Test ' + UUID.randomUUID(), url: 'http://test.org/' + UUID.randomUUID()]).save(flush: true)
        return feed
    }

    @Test
    void testList() {
        (1..11).each { i ->
            def entry = Entry.newInstance([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
            entry.save(flush: true, failOnError: true)
        }
        controller.params.max = 10
        def model = controller.list()

        assertTrue model.entryInstanceList.size() == 10
        assertTrue model.entryInstanceTotal > 10
    }

    @Test
    void testCreate() {
        controller.params.putAll([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        def model = controller.create()

        assertNotNull model.entryInstance
        assert model.entryInstance instanceof Entry
    }

    @Test
    void testSave() {
        controller.params.putAll([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        controller.save()
        def url = controller.response.redirectedUrl
        def urlArr = url.split('/')
        def id = urlArr[urlArr.length - 1]
        def entry = Entry.findById(id)

        assertEquals "/entry/show/" + id, controller.response.redirectedUrl
        assert entry
    }

    @Test
    void testShow() {
        def entry = Entry.newInstance([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        def model = controller.show(entry.id)

        assert entry.id == model.entryInstance.id
    }

    @Test
    void testEdit() {
        def entry = Entry.newInstance([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        def model = controller.edit(entry.id)

        assert entry.id == model.entryInstance.id
    }

    @Test
    void testUpdate() {
        def randomId = UUID.randomUUID()
        def entry = Entry.newInstance([title: 'Test ' + randomId, link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        def version = entry.version
        def updatedTitle = 'Updated ' + randomId
        controller.params.putAll([title: updatedTitle])
        controller.update(entry.id, version)
        def url = controller.response.redirectedUrl
        def urlArr = url.split('/')
        def id = urlArr[urlArr.length - 1]
        def updatedEntry = Entry.findById(id)

        assertEquals "/entry/show/" + id, controller.response.redirectedUrl
        assert updatedEntry.title == updatedTitle
    }

    @Test
    void testDelete() {
        def entry = Entry.newInstance([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        controller.delete(entry.id)
        def test = Entry.findById(entry.id)

        assertNull(test)
        assertEquals('/entry/list', controller.response.redirectedUrl)
    }

    @Test
    void testSearch() {
        def randomString = UUID.randomUUID().toString()
        def entry = Entry.newInstance([title: randomString, link: 'http://test.org/' + randomString, contents: randomString, postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        controller.params.q = randomString
        controller.search()

        assert renderMap.entriesTotal == 1 // because honestly - what are the freaking chances that there is another entry that has that UUID in it?!?!
    }

    @Test
    void testGo() {
        def entry = Entry.newInstance([title: 'Test ' + UUID.randomUUID(), link: 'http://test.org/' + UUID.randomUUID(), contents: UUID.randomUUID().toString(), postedOn: new Date(), feed: createFeed()])
        entry.save(flush: true, failOnError: true)
        controller.go(entry.id.toString())
        assert controller.response.redirectedUrl == entry.link
    }

    @Test
    void testByFeed() {
        fail('not yet implemented')
    }

    @Test
    void testByCategory() {
        fail('not yet implemented')
    }
}
