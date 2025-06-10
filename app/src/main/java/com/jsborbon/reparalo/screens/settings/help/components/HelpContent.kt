package com.jsborbon.reparalo.screens.settings.help.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.HelpItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HelpContent(
    helpItems: List<HelpItem>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .clearAndSetSemantics {
                contentDescription = "Lista de artículos de ayuda, ${helpItems.size} elementos"
            },
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = helpItems,
            key = { item -> item.hashCode() }
        ) { item ->
            HelpCard(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
