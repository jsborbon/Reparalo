package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.utils.ValidationUtils.validateCategory
import com.jsborbon.reparalo.utils.ValidationUtils.validateDescription
import com.jsborbon.reparalo.utils.ValidationUtils.validateTitle
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialCreateScreen(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val viewModel: TutorialsViewModel = viewModel()
    val context = LocalContext.current
    val createState by viewModel.createState.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var difficulty by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var videoUrl by rememberSaveable { mutableStateOf("") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }

    var visible by remember { mutableStateOf(false) }

    var currentUserId by remember { mutableStateOf("") }
    var currentUserName by remember { mutableStateOf("") }

    val isFormValid by remember {
        derivedStateOf {
            title.isNotBlank() && description.isNotBlank() &&
                category.isNotBlank() && difficulty.isNotBlank() &&
                duration.isNotBlank() && currentUserId.isNotBlank() &&
                titleError == null && descriptionError == null && categoryError == null
        }
    }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true

        viewModel.uiMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        auth.currentUser?.let { user ->
            currentUserId = user.uid
            val doc = firestore.collection("users").document(user.uid).get().await()
            currentUserName = doc.getString("name") ?: ""
        }
    }

    LaunchedEffect(createState) {
        if (createState is ApiResponse.Success) {
            delay(500)
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Tutorial") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_description),
                            contentDescription = "Ayuda"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .semantics { contentDescription = "Pantalla de creación de tutorial" }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        FormSection(title = "Información básica") {
                            OutlinedTextField(
                                value = title,
                                onValueChange = {
                                    title = it
                                    titleError = validateTitle(it)
                                },
                                label = { Text("Título") },
                                placeholder = { Text("Ej: Cómo reparar un grifo que gotea") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_title),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                isError = titleError != null,
                                supportingText = {
                                    titleError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("titleField")
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = {
                                    description = it
                                    descriptionError = validateDescription(it)
                                },
                                label = { Text("Descripción") },
                                placeholder = { Text("Describe paso a paso cómo realizar la reparación...") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_description),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                isError = descriptionError != null,
                                supportingText = {
                                    descriptionError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                minLines = 3,
                                maxLines = 5,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("descriptionField")
                            )
                        }

                        FormSection(title = "Clasificación") {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {
                                    category = it
                                    categoryError = validateCategory(it)
                                },
                                label = { Text("Categoría") },
                                placeholder = { Text("Ej: Fontanería, Electricidad, Carpintería...") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_category),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                isError = categoryError != null,
                                supportingText = {
                                    categoryError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("categoryField")
                            )

                            OutlinedTextField(
                                value = difficulty,
                                onValueChange = { difficulty = it },
                                label = { Text("Nivel de dificultad") },
                                placeholder = { Text("Ej: Fácil, Medio, Difícil") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_speed),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("difficultyField")
                            )
                        }

                        FormSection(title = "Detalles adicionales") {
                            OutlinedTextField(
                                value = duration,
                                onValueChange = { duration = it },
                                label = { Text("Duración estimada") },
                                placeholder = { Text("Ej: 30 min, 1 hora...") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_timer_arrow_up),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.None,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("durationField")
                            )

                            OutlinedTextField(
                                value = videoUrl,
                                onValueChange = { videoUrl = it },
                                label = { Text("URL del video (opcional)") },
                                placeholder = { Text("Ej: https://youtube.com/...") },
                                supportingText = { Text("Añade un enlace a un video explicativo si lo tienes") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("videoUrlField")
                            )
                        }

                        if (!isFormValid && (title.isNotBlank() || description.isNotBlank())) {
                            ValidationWarning(
                                message = "Por favor, completa todos los campos obligatorios correctamente antes de continuar."
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                focusManager.clearFocus()

                                if (currentUserId.isBlank() || currentUserName.isBlank()) {
                                    Toast.makeText(
                                        context,
                                        "No se pudo obtener la información del usuario.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                val tutorial = Tutorial(
                                    id = UUID.randomUUID().toString(),
                                    title = title.trim(),
                                    description = description.trim(),
                                    category = category.trim(),
                                    difficultyLevel = difficulty.trim(),
                                    estimatedDuration = duration.trim(),
                                    author = Author(uid = currentUserId, name = currentUserName),
                                    materials = emptyList(),
                                    averageRating = 0f,
                                    videoUrl = videoUrl.trim(),
                                    publicationDate = Date()
                                )
                                viewModel.createTutorial(tutorial)
                            },
                            enabled = createState !is ApiResponse.Loading && isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("createButton")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Crear Tutorial", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            if (createState is ApiResponse.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { contentDescription = "Cargando" },
                    contentAlignment = Alignment.Center
                ) {
                    LoadingState(
                        message = "Creando tutorial...",
                        showLinear = true
                    )
                }
            }
        }
    }
}

@Composable
fun FormSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            content()
        }
    }
}

@Composable
fun ValidationWarning(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
