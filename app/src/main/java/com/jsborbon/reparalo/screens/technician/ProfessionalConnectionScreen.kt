package com.jsborbon.reparalo.screens.technician

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.screens.technician.components.ProfessionalConnectionContent
import com.jsborbon.reparalo.viewmodels.TechnicianListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalConnectionScreen(
    navController: NavController,
) {
    val viewModel: TechnicianListViewModel = viewModel()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val technicianState by viewModel.technicians.collectAsState()

    var retryCount by remember { mutableIntStateOf(0) }
    var isContentVisible by remember { mutableStateOf(false) }

    val showFab by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 3 }
    }

    LaunchedEffect(retryCount) {
        viewModel.loadAllTechnicians()
    }

    LaunchedEffect(technicianState) {
        if (technicianState is ApiResponse.Success) {
            isContentVisible = true
        }
    }

    Scaffold(

        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.semantics {
                        contentDescription = "Volver al inicio de la lista"
                    }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.KeyboardArrowUp,
                        contentDescription = "Ir arriba"
                    )
                }
            }
        }
    ) { innerPadding ->

        ProfessionalConnectionContent(
            technicianState = technicianState,
            isContentVisible = isContentVisible,
            lazyListState = lazyListState,
            onTechnicianClick = { technician ->
                navController.navigate("technician_profile/${technician.uid}")
            },
            onRetry = { retryCount++ },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
