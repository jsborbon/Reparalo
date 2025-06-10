package com.jsborbon.reparalo.models

data class TermsAndConditions(
    val lastUpdated: String = "",
    val sections: List<TermsSection> = emptyList()
)
