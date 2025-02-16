package com.jsborbon.reparalo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.relato.models.CalendarEvent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarView(navController: NavController) {
    var selectedIndex = 1

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBottomBar(selectedIndex = selectedIndex, navController = navController)
        }
    ) { innerPadding ->
        CalendarViewContent(innerPadding)
    }
}

@Composable
fun CalendarViewContent(innerPadding: PaddingValues) {
    val selectedDate by remember { mutableStateOf(Date()) }
    var currentMonthOffset by remember { mutableIntStateOf(0) }
    var showNewEventDialog by remember { mutableStateOf(false) }
    val showDayDetail by remember { mutableStateOf(false) }

    val events = remember { mutableStateListOf<CalendarEvent>() }
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        firestore.collection("events").get()
            .addOnSuccessListener { querySnapshot ->
                events.clear()
                events.addAll(querySnapshot.documents.mapNotNull { it.toObject(CalendarEvent::class.java) })
            }
            .addOnFailureListener { exception ->
                println("Error fetching events: $exception")
            }
    }

    val calendar = Calendar.getInstance()
    val displayedMonth = calendar.apply {
        time = Date()
        add(Calendar.MONTH, currentMonthOffset)
    }.time


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewEventDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(Modifier.fillMaxSize().padding(it).padding(innerPadding)) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonthOffset -= 1 }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(displayedMonth),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { currentMonthOffset += 1 }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }

            // Weekday headers
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                    Text(text = it, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }

            // Days Grid
            val days = generateDaysInMonth(displayedMonth)
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(days.size) { index ->
                    val day = days[index]
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(Color(0xFF6A5ACD), shape = CircleShape)
                    ) {
                        if (day.date != null) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                color = if (day.isToday) Color.Red else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Show upcoming or day-specific events
            if (showDayDetail) {
                DayDetailView(selectedDate, events.filter {
                    it.date.toLocalDate() == selectedDate.toLocalDate()
                })
            }
        }
    }

    // New Event Dialog
    if (showNewEventDialog) {
        NewEventDialog(
            onDismiss = { showNewEventDialog = false },
            onEventAdded = { newEvent ->
                firestore.collection("events").add(newEvent)
                    .addOnSuccessListener {
                        events.add(newEvent)
                    }
                    .addOnFailureListener {
                        println("Error adding event: $it")
                    }
                showNewEventDialog = false
            }
        )
    }
}

// Models and helpers

data class Day(val date: Date?, val dayOfMonth: Int, val isToday: Boolean, val isSelected: Boolean)

fun generateDaysInMonth(monthDate: Date): List<Day> {
    val calendar = Calendar.getInstance().apply { time = monthDate }
    val today = Calendar.getInstance()
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK) - 1
    val totalCells = daysInMonth + firstDayOfMonth

    return List(totalCells) { index ->
        if (index < firstDayOfMonth) {
            Day(null, 0, false, false)
        } else {
            val day = index - firstDayOfMonth + 1
            val date = calendar.apply {
                set(Calendar.DAY_OF_MONTH, day)
            }.time
            Day(
                date = date,
                dayOfMonth = day,
                isToday = today.isSameDay(calendar),
                isSelected = false
            )
        }
    }
}

fun Calendar.isSameDay(other: Calendar): Boolean =
    this.get(Calendar.YEAR) == other.get(Calendar.YEAR) && this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

fun Date.toLocalDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this)

@Composable
fun DayDetailView(selectedDate: Date, events: List<CalendarEvent>) {
    val dateFormatter = remember {
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Events for ${dateFormatter.format(selectedDate)}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (events.isEmpty()) {
            Text(
                text = "No events for this day.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn {
                items(events) { event ->
                    EventCard(event)
                }
            }
        }
    }
}

@Composable
fun EventCard(event: CalendarEvent) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Created by: ${event.createdBy}",
                style = MaterialTheme.typography.displayMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NewEventDialog(
    onDismiss: () -> Unit,
    onEventAdded: (CalendarEvent) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    val date by remember { mutableStateOf(Date()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Event") },
        text = {
            Column {
                Text("Event Title")
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth().padding(4.dp).background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Created By")
                BasicTextField(
                    value = createdBy,
                    onValueChange = { createdBy = it },
                    modifier = Modifier.fillMaxWidth().padding(4.dp).background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Event Date")
                Button(onClick = { /* Add date picker logic */ }) {
                    Text("Select Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty() && createdBy.isNotEmpty()) {
                        val id = UUID.randomUUID()
                        val newEvent = CalendarEvent(id,date, title, createdBy)
                        onEventAdded(newEvent)
                    }
                }
            ) {
                Text("Add Event")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
