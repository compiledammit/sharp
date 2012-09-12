package com.sharp.agg.feed

class Category implements Comparable {
    String category

    static searchable = {
        root false
    }

    static constraints = {
        category(unique: true, maxSize: 100)
    }

    int compareTo(obj) {
        category.compareTo(obj.category)
    }
}
