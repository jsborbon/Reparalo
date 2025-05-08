package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.ForumViewModel

@Composable
fun ForumEditScreen(
    navController: NavController,
    topicId: String,
    viewModel: ForumViewModel = viewModel()
) {
    val topicState by viewModel.getTopicById(topicId).collectAsState(initial = ApiResponse.Loading)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    LaunchedEffect(topicState) {
        if (topicState is ApiResponse.Success) {
            val topic = (topicState as ApiResponse.Success).data
            title = topic.title
            description = topic.description
            category = topic.category
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tema del Foro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.editTopic(topicId, title, description, category)
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar cambios")
            }

            if (topicState is ApiResponse.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (topicState is ApiResponse.Failure) {
                Text(
                    text = "Error: ${(topicState as ApiResponse.Failure).errorMessage}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
