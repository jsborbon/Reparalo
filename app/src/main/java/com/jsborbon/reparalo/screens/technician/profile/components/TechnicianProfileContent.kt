package com.jsborbon.reparalo.screens.technician.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.components.states.SuccessState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.screens.technician.components.TechnicianInfoCard

/**
 * Technician profile content container.
 */
@Composable
fun TechnicianProfileContent(
    technicianState: ApiResponse<User>,
    isContentVisible: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (technicianState) {
            is ApiResponse.Loading -> {
                LoadingState(
                    message = "Cargando perfil del técnico...",
                    semanticDescription = "Cargando información del técnico"
                )
            }

            is ApiResponse.Success -> {
                val technician = technicianState.data
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = slideInVertically { it / 2 } + fadeIn(),
                    exit = fadeOut()
                ) {
                    SuccessState(
                        hasData = true,
                        semanticDescription = "Perfil técnico cargado"
                    ) {
                        TechnicianInfoCard(technician = technician)
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
                    title = "Perfil técnico",
                    message = "Preparando información del técnico...",
                    icon = androidx.compose.material.icons.Icons.Default.Person,
                    semanticDescription = "Esperando perfil técnico"
                )
            }
        }
    }
}
