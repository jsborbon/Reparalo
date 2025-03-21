package com.jsborbon.reparalo.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Review
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LatestReviews() {
    val reviews = remember { mutableStateListOf<Review>() }

    LaunchedEffect(Unit) {
        loadReviewsFromFirebaseOnce(
            onResult = {
                reviews.clear()
                reviews.addAll(it)
            },
            onError = {
                Log.d("Firebase", "Failed to load reviews", it)
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection()
        ReviewList(reviews = reviews)
    }
}

private fun loadReviewsFromFirebaseOnce(
    onResult: (List<Review>) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("reviews")
        .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .limit(10)
        .get()
        .addOnSuccessListener { documents ->
            val reviewsList = documents.mapNotNull { document ->
                try {
                    document.toObject(Review::class.java)
                } catch (e: Exception) {
                    Log.e("Firebase", "Error converting document", e)
                    null
                }
            }
            onResult(reviewsList)
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Latest Reviews",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Arrow",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ReviewList(reviews: List<Review>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(reviews) { review ->
            ReviewItem(review)
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Reviewer Icon",
                modifier = Modifier.size(30.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = review.reviewer,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = review.title,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Text(
                    text = review.reviewBody,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            repeat(review.starsGiven) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Filled Star",
                    tint = Color(0xFFFFCC00),
                    modifier = Modifier.size(20.dp)
                )
            }
            repeat(5 - review.starsGiven) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Empty Star",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}