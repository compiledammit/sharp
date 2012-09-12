package com.sharp.agg.feed

import org.junit.After
import org.junit.Before
import org.junit.Test

class FeedControllerTests extends BaseControllerTestCase {

    def feedService
    def entryService

    @Before
    void setUp() {
        super.setUp()
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
        assertEquals("list", controller.redirectArgs.action)
    }


    @Test
    void testList() {

        (1..11).each { i ->
            def model = createFeed([title: 'Test ' + i, url: 'http://test.org/' + i, isApproved: false])
            saveFeed(model.feedInstance)
        }
        def model = controller.list(10)
        assertTrue model.feedInstanceList.size() == 10
        assertTrue model.feedInstanceTotal > 10
    }

    @Test
    void testCreate() {
        def model = createFeed([title: 'Test ' + UUID.randomUUID().toString(), url: 'http://test.org/' + UUID.randomUUID().toString(), isApproved: false])
        assertNotNull model.feedInstance
        assert model.feedInstance instanceof Feed
    }

    @Test
    void testSave() {
        def model = createFeed([title: 'Test ' + UUID.randomUUID().toString(), url: 'http://test.org/' + UUID.randomUUID().toString(), isApproved: false])
        def savedModel = saveFeed(model.feedInstance)

        def feed = Feed.findById( controller.redirectArgs.id )

        assert controller.redirectArgs.action == 'show'
        assert feed
    }

    private saveFeed(Feed feed) {
        controller.params.putAll(feed.properties)
        return controller.save()
    }

    private createFeed(args) {
        controller.params.putAll(args)
        return controller.create()
    }
}
