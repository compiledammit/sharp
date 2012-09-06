package com.sharp.agg.feed

import com.sharp.CrudService
import org.codehaus.groovy.runtime.TimeCategory

class FeedService extends CrudService {

    /* get disregards hibernate filter */

    def get(Long id) {
        def feed
        Feed.withoutHibernateFilters {
            feed = Feed.findById(id)
        }
        return feed
    }

    def listAll(params) {
        params.max = Math.min(params.max as Integer ?: 10, 100)
        def feeds, feedCount
        Feed.withoutHibernateFilters {
            feeds = Feed.list(params)
            feedCount = Feed.count()
        }
        return [list: feeds, total: feedCount]
    }

    def findNeedingUpdate() {
        def hourAgo
        def now = new Date()

        use(TimeCategory) {
            hourAgo = now - 1.hours
        }

        def stale
        Feed.withHibernateFilter('approvedFilter') {
            stale = Feed.findAllByLastCheckedLessThan(hourAgo)
        }
        return stale
    }

    def findById(Long id) {
        def feed = Feed.findById(id)
        return feed
    }

    def delete(classRef, Long id) {
        def feed = Feed.get(id) // use get to bypass the hibernate filter.  could also use withoutHibernateFilters{} here i 'spose
        super.delete( feed )
    }
}
