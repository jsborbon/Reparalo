package com.jsborbon.relato.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jsborbon.relato.models.Review

@Composable
fun LatestReviews() {

    val db = FirebaseDatabase.getInstance()
    val coleccion = "reviews"
    val database = db.getReference(coleccion)
    val reviews = remember { mutableStateListOf<Review>() }


    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reviews.clear()
                snapshot.children.mapNotNullTo(reviews) { it.getValue(Review::class.java) }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Failed to read value.", error.toException())
            }
        })
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderSection()
        ReviewList(reviews = reviews)
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
            painter = painterResource(id = android.R.drawable.arrow_down_float),
            contentDescription = "Arrow",
            tint = Color(0xFF6200EE)
        )
    }
}

@Composable
fun ReviewList(reviews: List<Review>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
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
                painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                contentDescription = "Quote Icon",
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

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 8.dp)) {
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
