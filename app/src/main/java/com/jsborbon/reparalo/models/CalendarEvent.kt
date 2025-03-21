package com.jsborbon.reparalo.models

import java.util.*

data class CalendarEvent(
    val id: UUID = UUID.randomUUID(),
    val date: Date,
    val title: String,
    val createdBy: String

)