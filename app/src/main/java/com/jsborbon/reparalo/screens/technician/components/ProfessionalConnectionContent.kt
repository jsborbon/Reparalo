package com.jsborbon.reparalo.screens.technician.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.components.states.SuccessState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.screens.technician.searching.components.TechnicianListSection

@Composable
fun ProfessionalConnectionContent(
    technicianState: ApiResponse<List<User>>,
    isContentVisible: Boolean,
    lazyListState: LazyListState,
    onTechnicianClick: (User) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (technicianState) {
            is ApiResponse.Loading -> {
                LoadingState(
                    message = "Buscando profesionales disponibles...",
                    semanticDescription = "Cargando lista de profesionales"
                )
            }

            is ApiResponse.Success -> {
                val technicians = technicianState.data

                if (technicians.isEmpty()) {
                    EmptyState(isSearching = false)
                } else {
                    AnimatedVisibility(
                        visible = isContentVisible,
                        enter = slideInVertically { it / 2 } + fadeIn(),
                        exit = fadeOut()
                    ) {
                        SuccessState(
                            hasData = technicians.isNotEmpty(),
                            content = {
                                TechnicianListSection(
                                    technicians = technicians,
                                    lazyListState = lazyListState,
                                    onTechnicianClick = onTechnicianClick
                                )
                            }
                        )
                    }
                }
            }

            is ApiResponse.Failure -> {
                ErrorState(
                    message = technicianState.errorMessage,
                    onRetry = onRetry
                )
            }

            is ApiResponse.Idle -> {
                IdleState(
                    icon = androidx.compose.material.icons.Icons.Default.Person,
                    title = "Buscando profesionales",
                    message = "Preparando red de profesionales..."
                )
            }
        }
    }
}
