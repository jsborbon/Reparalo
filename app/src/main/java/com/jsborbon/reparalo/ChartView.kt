package com.jsborbon.reparalo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

data class ChartData(
    val id: java.util.UUID = java.util.UUID.randomUUID(),
    val views: Int,
    val date: java.time.LocalDate
)

class ChartViewModel : ViewModel() {
    var chartData by mutableStateOf(kotlin.collections.listOf<ChartData>())

    fun generateChartData() {
        val tempChartData = mutableListOf<ChartData>()
        for (i in 1..12) { // Generate data for 12 months
            val date = LocalDate.now().minusMonths(i.toLong())
            val newData = ChartData(views = Random.nextInt(0, 1000), date = date)
            tempChartData.add(newData)
        }
        chartData = tempChartData.sortedBy { it.date }
    }

    fun totalNumberOfViews(): Int = chartData.sumOf { it.views }
}

@Composable
fun ChartView() {
    val vm = remember { ChartViewModel() }
    val numberFormat = DecimalFormat("#,###.##")

    // Generate data when the Composable is first displayed
    LaunchedEffect(Unit) {
        vm.generateChartData()
    }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = numberFormat.format(vm.totalNumberOfViews()),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(text = "Visits this year")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Render a custom Column Chart
            CustomColumnChart(
                data = vm.chartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }



@Composable
fun CustomColumnChart(data: List<ChartData>, modifier: Modifier = Modifier) {
    val maxValue = data.maxOfOrNull { it.views } ?: 1

    Canvas(modifier = modifier) {
        val chartWidth = size.width
        val chartHeight = size.height

        val barWidth = chartWidth / (data.size * 2)
        val barSpacing = barWidth

        data.forEachIndexed { index, chartData ->
            val barHeight = (chartData.views.toFloat() / maxValue) * chartHeight

            val startX = (index * (barWidth + barSpacing)) + barSpacing / 2

            drawRect(
                brush = Brush.verticalGradient(colors = listOf(Color.Cyan, Color.Blue)),
                topLeft = Offset(startX, chartHeight - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            drawContext.canvas.nativeCanvas.drawText(
                chartData.date.month.name.substring(0, 3),
                startX + barWidth / 4,
                chartHeight + 20,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f

                }

            )
        }
    }
}
