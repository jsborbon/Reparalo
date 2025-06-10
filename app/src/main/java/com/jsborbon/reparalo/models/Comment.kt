package com.jsborbon.reparalo.models

import java.util.Date

data class Comment(
    val id: String = "",
    val tutorialId: String? = null,
    val forumTopicId: String? = null,
    val author: Author = Author(),
    val content: String = "",
    val date: Date = Date(),
    val rating: Int? = null
)
