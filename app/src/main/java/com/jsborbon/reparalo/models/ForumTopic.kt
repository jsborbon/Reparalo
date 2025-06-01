package com.jsborbon.reparalo.models

import java.util.Date

data class ForumTopic(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val author: Author = Author(),
    val category: String = "",
    val date: Date = Date(),
    val preview: String = "",
    val comments: Int = 0,
    val likes: Int = 0,
    val views: Int = 0,
)
