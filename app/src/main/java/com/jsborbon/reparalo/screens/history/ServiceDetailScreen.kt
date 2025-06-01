package com.jsborbon.reparalo.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.utils.formatDate
import com.jsborbon.reparalo.viewmodels.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    navController: NavController,
    serviceId: String,
    viewModel: HistoryViewModel = remember { HistoryViewModel() },
) {
    LaunchedEffect(serviceId) {
        viewModel.loadService(serviceId)
    }

    val result by viewModel.serviceDetail.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle del servicio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (result) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is ApiResponse.Success -> {
                    val item = (result as ApiResponse.Success).data
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = formatDate(item.date),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                is ApiResponse.Failure -> {
                    val errorMessage = (result as ApiResponse.Failure).errorMessage
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
