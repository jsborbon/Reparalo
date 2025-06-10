package com.jsborbon.reparalo.screens.settings.terms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.screens.settings.terms.components.ReadingOptionsPanel
import com.jsborbon.reparalo.screens.settings.terms.components.ReadingProgressBar
import com.jsborbon.reparalo.screens.settings.terms.components.TermsContent
import com.jsborbon.reparalo.viewmodels.TermsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTermsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val viewModel: TermsViewModel = viewModel()
    val termsState by viewModel.terms.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Reading preferences
    var textSize by remember { mutableStateOf(16f) }
    var showReadingProgress by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }

    // Reading progress calculation
    val readingProgress by remember {
        derivedStateOf {
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val totalItems = listState.layoutInfo.totalItemsCount

            if (totalItems == 0) 0f
            else (firstVisibleIndex.toFloat() / totalItems * 100).coerceIn(0f, 100f)
        }
    }

    // Show scroll to top button
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    // Animated text size
    val animatedTextSize by animateDpAsState(
        targetValue = textSize.dp,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "text_size"
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Términos y Condiciones",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_format_color_text),
                            contentDescription = "Opciones de lectura"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring()) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring()) + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        scope.launch { listState.animateScrollToItem(0) }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_expand_circle_up),
                            contentDescription = "Ir arriba"
                        )
                    },
                    text = { Text("Ir arriba") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 12.dp
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ReadingOptionsPanel(
                    textSize = textSize,
                    onTextSizeChange = { textSize = it },
                    showProgress = showReadingProgress,
                    onShowProgressChange = { showReadingProgress = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedVisibility(
                visible = showReadingProgress && termsState is ApiResponse.Success,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ReadingProgressBar(
                    progress = readingProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedContent(
                targetState = termsState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "terms_content"
            ) { state ->
                when (state) {
                    is ApiResponse.Loading -> {
                        LoadingState(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is ApiResponse.Failure -> {
                        ErrorState(
                            message = state.errorMessage,
                            onRetry = { viewModel.loadTerms() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is ApiResponse.Success -> {
                        TermsContent(
                            terms = state.data,
                            textSize = animatedTextSize,
                            listState = listState,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is ApiResponse.Idle -> {
                        IdleState(
                            painter = painterResource(id = R.drawable.baseline_info),
                            title = "Sin términos disponibles",
                            message = "Por favor intenta de nuevo más tarde.",
                            buttonText = "Recargar",
                            onAction = { viewModel.loadTerms() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
