package com.sharp.agg.feed

class Category {
    String category

    static searchable = {
        root false
    }

    static constraints = {
        category(unique: true, maxSize: 100)
    }
}
