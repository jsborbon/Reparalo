package com.jsborbon.reparalo.screens.forum

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ForumTopic
import com.jsborbon.reparalo.viewmodels.ForumViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumEditScreen(
    navController: NavController,
    topicId: String,
    viewModel: ForumViewModel = hiltViewModel()
) {
    val topicFlow: Flow<ApiResponse<ForumTopic>> = viewModel.getTopicById(topicId)
    val topicState by topicFlow.collectAsState(initial = ApiResponse.Loading)
    val editState by viewModel.editState.collectAsState()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    LaunchedEffect(topicState) {
        if (topicState is ApiResponse.Success) {
            val topic = (topicState as ApiResponse.Success<ForumTopic>).data
            title = topic.title
            description = topic.description
            category = topic.category
        }
    }

    LaunchedEffect(editState) {
        when (editState) {
            is ApiResponse.Success -> {
                Toast.makeText(context, "Tema editado con éxito", Toast.LENGTH_SHORT).show()
                viewModel.resetEditState()
                navController.popBackStack()
            }

            is ApiResponse.Failure -> {
                val errorMessage = (editState as ApiResponse.Failure).errorMessage
                Toast.makeText(context, "Error al editar: $errorMessage", Toast.LENGTH_SHORT).show()
                viewModel.resetEditState()
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tema del Foro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
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
                },
                modifier = Modifier.align(Alignment.End),
                enabled = editState !is ApiResponse.Loading
            ) {
                Text("Guardar cambios")
            }

            if (topicState is ApiResponse.Loading || editState is ApiResponse.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (topicState is ApiResponse.Failure) {
                val errorMessage = (topicState as ApiResponse.Failure).errorMessage
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
