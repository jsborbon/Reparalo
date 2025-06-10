package com.jsborbon.reparalo.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ServiceHistoryItem
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.history.components.ServiceHistoryCard
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.viewmodels.HistoryViewModel

@Composable
fun ServiceHistoryScreen(
    navController: NavController,
) {
    val viewModel: HistoryViewModel = viewModel()
    val state by viewModel.historyItems.collectAsState()

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        viewModel.fetchServiceHistory(userId = userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id=R.drawable.baseline_history),
                contentDescription = null,
                tint = PrimaryLight,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Historial de servicios",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryLight
            )
        }

        when (state) {
            is ApiResponse.Idle -> LoadingState(message = "Preparando historial de servicios")
            is ApiResponse.Loading -> LoadingState(message = "Cargando historial de servicios")

            is ApiResponse.Success -> {
                val successState = state as ApiResponse.Success<List<ServiceHistoryItem>>
                val historyItems = successState.data

                if (historyItems.isEmpty()) {
                    EmptyState(
                        isSearching = false
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(
                            items = historyItems,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    initialOffsetY = { it / 3 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        delayMillis = (index * 50).coerceAtMost(500)
                                    )
                                ) + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        delayMillis = (index * 50).coerceAtMost(500)
                                    )
                                )
                            ) {
                                ServiceHistoryCard(
                                    title = item.title,
                                    description = item.description,
                                    date = item.date,
                                    showButton = true,
                                    buttonText = "Ver detalles",
                                    onButtonClick = {
                                        navController.navigate(Routes.serviceDetailWithId(item.id))
                                    },
                                    status = determineServiceStatus(item),
                                    modifier = Modifier.semantics {
                                        contentDescription = "Servicio: ${item.title}, del ${com.jsborbon.reparalo.utils.formatDate(item.date)}"
                                    }
                                )
                            }
                        }
                    }
                }
            }

            is ApiResponse.Failure -> {
                val failure = state as ApiResponse.Failure
                ErrorState(
                    message = failure.errorMessage,
                    onRetry = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                        viewModel.fetchServiceHistory(userId = userId)
                    }
                )
            }
        }
    }
}
