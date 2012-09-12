package com.sharp.agg.feed

class EntryView {
    String remoteAddress
    Date dateCreated

    static belongsTo = [entry: Entry]

    static constraints = {
        remoteAddress(nullable: true)
    }
}
