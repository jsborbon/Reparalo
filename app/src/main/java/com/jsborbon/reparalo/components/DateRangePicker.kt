package com.jsborbon.reparalo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DateRangePickerView() {
    var fromDate by remember { mutableStateOf(Calendar.getInstance().time) }
    var toDate by remember { mutableStateOf(Calendar.getInstance().time) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // From Date Picker
        Text("From Date")
        DatePicker(fromDate) { selectedDate -> fromDate = selectedDate }

        Spacer(modifier = Modifier.height(16.dp))

        // Selection Display
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Selection: ")
            Text(formatDate(fromDate))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.Blue
            )
            Text(formatDate(toDate), color = Color.Blue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("To Date")
        DatePicker(toDate, minDate = fromDate) { selectedDate -> toDate = selectedDate }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Close the view or take action */ }) {
            Text("Done")
        }
    }
}

@Composable
fun DatePicker(selectedDate: Date, minDate: Date? = null, onDateChange: (Date) -> Unit) {
    val calendar = Calendar.getInstance().apply { time = selectedDate }
    val datePickerDialog = remember { mutableStateOf(false) }

    if (datePickerDialog.value) {
       val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        DatePickerDialog(
            initialDate = calendar.time,
            minDate = minDate,
            onDateSelected = { date ->
                onDateChange(date)
                datePickerDialog.value = false
            }
        )
    }

    Button(onClick = { datePickerDialog.value = true }) {
        Text("Pick Date")
    }
}

@Composable
fun DatePickerDialog(
    initialDate: Date,
    minDate: Date?,
    onDateSelected: (Date) -> Unit
) {
    Text("Show DatePicker Dialog here")
}

fun formatDate(date: Date): String {
    val format = SimpleDateFormat("yyyy MMM dd", Locale.getDefault())
    return format.format(date)
}

