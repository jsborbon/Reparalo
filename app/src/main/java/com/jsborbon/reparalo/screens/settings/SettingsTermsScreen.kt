package com.jsborbon.reparalo.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.TermsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTermsScreen(
    navController: NavController,
    viewModel: TermsViewModel = remember { TermsViewModel() }
) {
    val termsState by viewModel.terms.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (val state = termsState) {
                is ApiResponse.Loading -> {
                    Text("Cargando términos y condiciones...", style = MaterialTheme.typography.bodyMedium)
                }

                is ApiResponse.Failure -> {
                    Text(
                        text = "Error: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is ApiResponse.Success -> {
                    val terms = state.data
                    Text(
                        text = "Última actualización: ${terms.lastUpdated}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    terms.sections.forEach { section ->
                        SectionTitle(title = section.title)
                        SectionText(text = section.content)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Text(
                        text = "Al continuar usando esta aplicación, aceptas y te comprometes a cumplir estos términos y condiciones.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun SectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
