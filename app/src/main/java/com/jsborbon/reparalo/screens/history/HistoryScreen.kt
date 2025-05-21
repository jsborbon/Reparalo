package com.jsborbon.reparalo.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ServiceHistoryItem
import com.jsborbon.reparalo.viewmodels.HistoryViewModel

@Composable
fun ServiceHistoryScreen(
    navController: NavController, //TODO
    viewModel: HistoryViewModel = viewModel(),
) {
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
        Text(
            text = "Historial de servicios",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        when (state) {
            is ApiResponse.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponse.Success -> {
                val historyItems = (state as ApiResponse.Success<List<ServiceHistoryItem>>).data
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(historyItems) { item ->
                        ServiceHistoryCard(
                            title = item.title,
                            description = item.description,
                            date = item.date,
                        )
                    }
                }
            }

            is ApiResponse.Failure -> {
                val errorMessage = (state as ApiResponse.Failure).errorMessage
                Text(
                    text = "Error al cargar el historial: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
