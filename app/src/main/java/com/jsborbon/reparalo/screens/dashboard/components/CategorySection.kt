
package com.jsborbon.reparalo.screens.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.Error
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Success

@Composable
fun CategorySection(navController: NavController) {
    val animationStates = remember {
        List(4) { mutableStateOf(false) }
    }

    // Staggered animation trigger
    LaunchedEffect(Unit) {
        animationStates.forEachIndexed { index, state ->
            kotlinx.coroutines.delay(index * 100L)
            state.value = true
        }
    }

    // First row
    Row(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = animationStates[0].value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        ) {
            CategoryCard(
                title = "Tutoriales",
                iconPainter = painterResource(id= R.drawable.outline_play_lesson),
                backgroundColor = PrimaryLight,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Routes.TUTORIALS) },
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        AnimatedVisibility(
            visible = animationStates[1].value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        ) {
            CategoryCard(
                title = "Materiales",
                icon = Icons.Default.Build,
                backgroundColor = RepairYellow,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Routes.MATERIALS_LIST) },
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Second row
    Row(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = animationStates[2].value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        ) {
            CategoryCard(
                title = "Profesionales",
                icon = Icons.Default.Person,
                backgroundColor = Success,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Routes.PROFESSIONAL_CONNECTION) },
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        AnimatedVisibility(
            visible = animationStates[3].value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        ) {
            CategoryCard(
                title = "Foro",
                iconPainter = painterResource(id= R.drawable.outline_forum),
                backgroundColor = Error,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Routes.FORUM) },
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))
}
