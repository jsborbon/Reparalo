package com.jsborbon.reparalo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comentario
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDetailScreen(navController: NavController, tutorialId: String) {
    val viewModel: TutorialDetailViewModel = viewModel()
    val tutorialState = viewModel.tutorial.collectAsState()
    val comentariosState = viewModel.comentarios.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val screenKey = currentBackStackEntry?.destination?.route

    key(screenKey) {
        val visibleState = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.cargarTutorial(tutorialId)
            viewModel.cargarComentarios(tutorialId)
            visibleState.value = true
        }

        AnimatedVisibility(
            visible = visibleState.value,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detalle del Tutorial") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* Compartir */ }) {
                                Icon(Icons.Default.Share, contentDescription = "Compartir")
                            }
                            IconButton(onClick = { /* Favorito */ }) {
                                Icon(Icons.Default.Favorite, contentDescription = "Favorito")
                            }
                        },
                    )
                },
            ) { innerPadding ->
                when (val tutorialResponse = tutorialState.value) {
                    is ApiResponse.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ApiResponse.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Error al cargar el tutorial")
                                Text(tutorialResponse.errorMessage, color = MaterialTheme.colorScheme.error)
                                Button(onClick = { viewModel.cargarTutorial(tutorialId) }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    is ApiResponse.Success -> {
                        val tutorial = tutorialResponse.data
                        TutorialDetailContent(
                            tutorial = tutorial,
                            comentariosState = comentariosState.value,
                            viewModel = viewModel,
                            innerPadding = innerPadding,
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TutorialDetailContent(
    tutorial: Tutorial,
    comentariosState: ApiResponse<List<Comentario>>,
    viewModel: TutorialDetailViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {
    val comentarioTexto = remember { mutableStateOf("") }
    val calificacion = remember { mutableStateOf(5) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // Video del tutorial
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.Black),
            ) {
                // Aquí se implementaría la reproducción del video
                // Por ahora, mostramos un placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Reproducir video",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White,
                    )
                }
            }
        }

        // Título y detalles del tutorial
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tutorial.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tutorial.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "•", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tutorial.nivelDificultad,
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (tutorial.nivelDificultad) {
                            "Fácil" -> Color.Green
                            "Intermedio" -> RepairYellow
                            "Avanzado" -> Color.Red
                            else -> Color.Gray
                        },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RepairYellow,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", tutorial.calificacionPromedio),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Duración estimada: ${tutorial.duracionEstimada}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Por: ${tutorial.autor}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tutorial.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        // Materiales necesarios
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Materiales necesarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        tutorial.materiales.forEachIndexed { index, material ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${material.nombre} (${material.cantidad})",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            if (index < tutorial.materiales.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        // Sección de comentarios (formulario)
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Comentarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Deja tu comentario",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = comentarioTexto.value,
                            onValueChange = { comentarioTexto.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Escribe tu comentario aquí") },
                            minLines = 3,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Calificación:",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            for (i in 1..5) {
                                IconButton(onClick = { calificacion.value = i }, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Calificación $i",
                                        tint = if (i <= calificacion.value) {
                                            RepairYellow
                                        } else {
                                            Color.Gray.copy(
                                                alpha = 0.5f,
                                            )
                                        },
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (comentarioTexto.value.isNotBlank()) {
                                    viewModel.enviarComentario(
                                        tutorialId = tutorial.id,
                                        texto = comentarioTexto.value,
                                        calificacion = calificacion.value,
                                    )
                                    comentarioTexto.value = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text("Enviar comentario")
                        }
                    }
                }
            }
        }

        // Lista de comentarios
        item {
            when (val comentariosResponse = comentariosState) {
                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ApiResponse.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error al cargar comentarios")
                            Text(
                                text = comentariosResponse.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.cargarComentarios(tutorial.id) }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is ApiResponse.Success -> {
                    val comentarios = comentariosResponse.data
                    if (comentarios.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Sé el primero en comentar este tutorial.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            comentarios.forEachIndexed { index, comentario ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        ),
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            val nombre =
                                                viewModel.nombresUsuarios.collectAsState().value[comentario.uidUsuario]
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .clip(RoundedCornerShape(18.dp))
                                                        .background(MaterialTheme.colorScheme.primary),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text(
                                                        text = (nombre ?: "U").take(1).uppercase(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = nombre ?: "Usuario anónimo",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = comentario.contenido,
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                repeat(comentario.calificacion) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = null,
                                                        tint = RepairYellow,
                                                        modifier = Modifier.size(16.dp),
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                                        .format(comentario.fecha),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.Gray,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
