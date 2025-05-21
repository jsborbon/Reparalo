package com.jsborbon.reparalo.models

import java.util.Date

data class ForumTopic(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val category: String,
    val date: Date,
    val preview: String = "",
    val comments: Int,
    val likes: Int,
    val views: Int,
)
