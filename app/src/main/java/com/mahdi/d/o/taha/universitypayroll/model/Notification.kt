package com.mahdi.d.o.taha.universitypayroll.model

data class Notification(
    val title: String,
    val msg: String,
    val sender: String,
) {
    constructor(
        title: String,
        msg: String,
        sender: String,
        receiver: String
    ) : this(title, msg, sender)
}