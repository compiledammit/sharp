package com.sharp.agg.feed

import org.junit.After
import org.junit.Before
import org.junit.Test

class FeedControllerTests extends BaseControllerTestCase {

    def feedService
    def entryService
    def controller

    @Before
    void setUp() {
        super.setUp()
        controller = new FeedController()
        controller.feedService = feedService
        controller.entryService = entryService

    }

    @After
    void tearDown() {
        super.tearDown()
    }

    @Test
    void testIndex() {
        controller.index()
        assertEquals("/feed/list", controller.response.redirectedUrl)
    }


    @Test
    void testList() {
        (1..11).each { i ->
            def feed = Feed.newInstance([title: 'Test ' + i, url: 'http://test.org/' + i, isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
            feed.save(flush: true, failOnError: true)
        }
        controller.params.max = 10
        def model = controller.list()
        assertTrue model.feedInstanceList.size() == 10
        assertTrue model.feedInstanceTotal > 10
    }

    @Test
    void testCreate() {
        controller.params.putAll([title: 'Test ' + UUID.randomUUID().toString(), url: 'http://test.org/' + UUID.randomUUID().toString(), isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        def model = controller.create()
        assertNotNull model.feedInstance
        assert model.feedInstance instanceof Feed
    }

    @Test
    void testSave() {
        controller.params.putAll([title: 'Test ' + UUID.randomUUID().toString(), url: 'http://test.org/' + UUID.randomUUID().toString(), isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        controller.save()

        def url = controller.response.redirectedUrl
        def urlArr = url.split('/')
        def id = urlArr[urlArr.length - 1]

        def feed = Feed.findById(id)

        assertEquals "/feed/show/" + id, controller.response.redirectedUrl
        assert feed
    }

    @Test
    void testSuggestFeed() {
        def model = controller.suggestFeed();
        assert model.feedInstance instanceof Feed
    }

    @Test
    void testSaveSuggestFeed() {
        def beforeSuggest = Feed.count()
        controller.params.putAll([title: 'Test', url: 'http://test.org'])
        controller.saveSuggestFeed();
        def afterSuggest = Feed.count()
        def u = controller.response.redirectedUrl
        assertEquals('/', u)
        assert afterSuggest == beforeSuggest + 1
    }

    @Test
    void testShow() {
        def feed = Feed.newInstance([title: 'Test ' + UUID.randomUUID(), url: 'http://test.org/' + UUID.randomUUID(), isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        feed.save(flush: true, failOnError: true)

        def model = controller.show(feed.id)

        assert feed.id == model.feedInstance.id
    }

    @Test
    void testEdit() {
        def feed = Feed.newInstance([title: 'Test ' + UUID.randomUUID(), url: 'http://test.org/' + UUID.randomUUID(), isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        feed.save(flush: true, failOnError: true)

        def model = controller.edit(feed.id)

        assert feed.id == model.feedInstance.id
    }

    @Test
    void testUpdate() {
        def randomId = UUID.randomUUID()
        def feed = Feed.newInstance([title: 'Test ' + randomId, url: 'http://test.org/' + randomId, isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        feed.save(flush: true, failOnError: true)
        def version = feed.version
        def updatedTitle = 'Updated ' + randomId
        controller.params.putAll([title: updatedTitle])
        controller.update(feed.id, version)

        def url = controller.response.redirectedUrl
        def urlArr = url.split('/')
        def id = urlArr[urlArr.length - 1]
        def updatedFeed = Feed.findById(id)

        assertEquals "/feed/show/" + id, controller.response.redirectedUrl
        assert updatedFeed.title == updatedTitle
    }

    @Test
    void testDelete() {
        def feed = Feed.newInstance([title: 'Test ' + UUID.randomUUID(), url: 'http://test.org/' + UUID.randomUUID(), isApproved: false, dateCreated: new Date(), lastChecked: new Date(), lastUpdated: new Date()])
        feed.save(flush: true, failOnError: true)

        controller.delete(feed.id)

        def test = Feed.findById(feed.id)
        assertNull(test)
        assertEquals('/feed/list', controller.response.redirectedUrl)
    }
}
