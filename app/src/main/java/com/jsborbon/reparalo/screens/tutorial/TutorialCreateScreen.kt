package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialCreateScreen(
    navController: NavController,
    viewModel: TutorialsViewModel = viewModel(),
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    val context = LocalContext.current
    val createState by viewModel.createState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    var currentUserId by remember { mutableStateOf("") }
    var currentUserName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        auth.currentUser?.let { user ->
            currentUserId = user.uid
            val doc = firestore.collection("users").document(user.uid).get().await()
            currentUserName = doc.getString("name") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Tutorial") })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("Nivel de dificultad") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración estimada") },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (currentUserId.isBlank() || currentUserName.isBlank()) {
                        Toast.makeText(
                            context,
                            "No se pudo obtener la información del usuario.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        return@Button
                    }
                    val tutorial = Tutorial(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        category = category,
                        difficultyLevel = difficulty,
                        estimatedDuration = duration,
                        author = Author(uid = currentUserId, name = currentUserName),
                        materials = emptyList(),
                        averageRating = 0f,
                        videoUrl = "",
                        publicationDate = Date(),
                    )
                    viewModel.createTutorial(tutorial)
                },
                enabled = createState !is ApiResponse.Loading,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Crear")
            }

            when (createState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ApiResponse.Success -> {
                    LaunchedEffect(Unit) {
                        viewModel.resetCreateState()
                        navController.popBackStack()
                    }
                }
                is ApiResponse.Failure -> {
                    LaunchedEffect((createState as ApiResponse.Failure).errorMessage) {
                        viewModel.resetCreateState()
                    }
                }
                null -> {}
            }
        }
    }
}
