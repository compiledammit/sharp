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

}
