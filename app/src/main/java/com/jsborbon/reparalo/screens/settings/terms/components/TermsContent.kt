package com.jsborbon.reparalo.screens.settings.terms.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TermsContent(
    terms: Any,
    textSize: Dp,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .clearAndSetSemantics {
                contentDescription = "Términos y condiciones, documento legal"
            },
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item(key = "header") {
            TermsHeader(
                lastUpdated = "terms.lastUpdated", // Replace with actual property
                textSize = textSize
            )
        }

        // Table of contents (if sections exist)
        item(key = "toc") {
            TableOfContents(
                sections = emptyList(), // Replace with terms.sections
                onSectionClick = { index ->
                    // Scroll to section
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Sections - Replace this with actual section iteration
        itemsIndexed(
            items = listOf("Section 1", "Section 2", "Section 3"), // Replace with terms.sections
            key = { index, _ -> "section_$index" }
        ) { index, section ->
            TermsSection(
                title = "section.title", // Replace with actual property
                content = "section.content", // Replace with actual property
                textSize = textSize,
                sectionNumber = index + 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Footer
        item(key = "footer") {
            TermsFooter(
                textSize = textSize,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Add some bottom padding for FAB
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
