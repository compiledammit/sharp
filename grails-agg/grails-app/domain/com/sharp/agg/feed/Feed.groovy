package com.sharp.agg.feed

import com.sharp.agg.user.User

class Feed {
    String title
    String url
    Date dateCreated
    Date lastUpdated
    User createdBy
    Date lastChecked
    Boolean isApproved = false

    static hibernateFilters = {
        approvedFilter(condition:'is_approved=1', default: true)
    }

    static searchable = {
        root false
    }

    static hasMany = [entries: Entry]

    static constraints = {
        title(blank: false, maxSize: 250, unique: true);
        url(blank: false, maxSize: 1000, unique: true, url: true)
        lastChecked(nullable: true)
        createdBy(nullable: true)
        isApproved(blank: false)
    }
}
