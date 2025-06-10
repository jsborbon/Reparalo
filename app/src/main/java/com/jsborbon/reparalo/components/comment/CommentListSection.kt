package com.jsborbon.reparalo.components.comment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment

@Composable
fun CommentListSection(
    commentsState: ApiResponse<List<Comment>>,
    userNamesMap: Map<String, String> = emptyMap(),
    onRetry: (() -> Unit)? = null,
) {
    val listState = rememberLazyListState()

    val showHeaderElevation by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset > 50 }
    }

    val headerElevation by animateFloatAsState(
        targetValue = if (showHeaderElevation) 4.dp.value else 2.dp.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "headerElevation"
    )

    AnimatedContent(
        targetState = commentsState,
        label = "commentsStateTransition",
        transitionSpec = {
            slideInVertically(initialOffsetY = { it / 4 }, animationSpec = spring()) +
                fadeIn(animationSpec = tween(500)) togetherWith
                slideOutVertically(targetOffsetY = { -it / 4 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
        }
    ) { currentState ->
        when (currentState) {
            is ApiResponse.Idle, is ApiResponse.Loading -> {
                LoadingState(message = "Cargando comentarios...")
            }

            is ApiResponse.Failure -> {
                ErrorState(
                    message = currentState.errorMessage,
                    onRetry = onRetry
                )
            }

            is ApiResponse.Success -> {
                val comments = currentState.data
                if (comments.isEmpty()) {
                    EmptyState(isSearching = false)
                } else {
                    CommentsListContent(
                        comments = comments,
                        userNames = userNamesMap,
                        listState = listState,
                        headerElevation = headerElevation.dp
                    )
                }
            }
        }
    }
}
@Composable
fun CommentListSection(
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    if (comments.isEmpty()) {
        EmptyState(isSearching = false)
    } else {
        CommentsListContent(
            comments = comments,
            userNames = emptyMap(),
            listState = rememberLazyListState(),
            headerElevation = 2.dp
        )
    }
}
