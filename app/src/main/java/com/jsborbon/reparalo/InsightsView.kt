package com.jsborbon.reparalo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavHostController

@Composable
fun InsightsView(navHostController: NavHostController) {
    Text(text = "Hello, World!", style = MaterialTheme.typography.bodyLarge)
}
