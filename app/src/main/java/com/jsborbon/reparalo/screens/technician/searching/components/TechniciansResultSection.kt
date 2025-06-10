package com.jsborbon.reparalo.screens.technician.searching.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.ui.components.LoadingIndicator

@Composable
fun TechniciansResultSection(
    techniciansState: ApiResponse<List<User>>,
    searchQuery: String,
    isSearchActive: Boolean,
    onRetry: () -> Unit,
    onTechnicianClick: ((User) -> Unit)? = null
) {
    val listState = rememberLazyListState()

    when (techniciansState) {
        is ApiResponse.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Cargando técnicos" },
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is ApiResponse.Failure -> {
            ErrorState(
                message = techniciansState.errorMessage,
                onRetry = onRetry
            )
        }

        is ApiResponse.Success -> {
            val filteredTechnicians = filterTechnicians(techniciansState.data, searchQuery)

            if (filteredTechnicians.isEmpty()) {
                EmptyState(
                    isSearching = isSearchActive,
                    searchQuery = searchQuery,
                    onClearSearch = if (isSearchActive) onRetry else null
                )
            } else {
                TechnicianListSection(
                    technicians = filteredTechnicians,
                    lazyListState = listState,
                    onTechnicianClick = onTechnicianClick
                )
            }
        }

        is ApiResponse.Idle -> {
            IdleState(
                title = "Búsqueda de técnicos",
                message = "Preparando lista de técnicos...",
                icon = Icons.Default.Person,
                semanticDescription = "Esperando datos de técnicos"
            )
        }
    }
}

fun filterTechnicians(list: List<User>, query: String): List<User> {
    if (query.isBlank()) return list

    val searchTerm = query.trim().lowercase()

    return list.filter { technician ->
        technician.name.lowercase().contains(searchTerm) ||
            technician.email.lowercase().contains(searchTerm) ||
            technician.specialty?.lowercase()?.contains(searchTerm) == true
    }
}
