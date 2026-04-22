package com.habitflow.newapp.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(viewModel: HabitViewModel) {
    val colors = AppTheme.colors
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.US)
    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val today = Calendar.getInstance()

    val monthCal = currentMonth.clone() as Calendar
    monthCal.set(Calendar.DAY_OF_MONTH, 1)
    val startDay = monthCal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    fun getDateCompletion(day: Int): Float {
        val cal = currentMonth.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, day)
        val key = sdf.format(cal.time)
        val dayComps = viewModel.completions[key] ?: emptyMap()
        val completed = dayComps.size
        return if (viewModel.habits.isEmpty()) 0f else completed.toFloat() / viewModel.habits.size
    }

    fun getHeatColor(ratio: Float): Color {
        return when {
            ratio == 0f -> colors.bgSecondary
            ratio <= 0.25f -> Indigo500.copy(alpha = 0.15f)
            ratio <= 0.5f -> Indigo500.copy(alpha = 0.3f)
            ratio <= 0.75f -> Indigo500.copy(alpha = 0.55f)
            else -> Indigo500.copy(alpha = 0.85f)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Calendar", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
            Text("Monthly overview of your habit consistency", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp, bottom = 4.dp))
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                    }) {
                        Icon(Icons.Default.ChevronLeft, null, tint = colors.textSecondary)
                    }
                    Text(
                        monthFormat.format(currentMonth.time),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700,
                        color = colors.textPrimary
                    )
                    IconButton(onClick = {
                        currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                    }) {
                        Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    dayNames.forEach { d ->
                        Text(
                            d,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W600,
                            color = colors.textMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val totalCells = startDay + daysInMonth
                val rows = (totalCells + 6) / 7

                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0..6) {
                            val cellIndex = row * 7 + col
                            val dayNum = cellIndex - startDay + 1

                            if (dayNum in 1..daysInMonth) {
                                val ratio = getDateCompletion(dayNum)
                                val cal = currentMonth.clone() as Calendar
                                cal.set(Calendar.DAY_OF_MONTH, dayNum)
                                val isToday = cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                        cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
                                val isSelected = selectedDate?.let {
                                    it.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                                            it.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
                                } ?: false

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) Indigo500
                                            else getHeatColor(ratio)
                                        )
                                        .then(
                                            if (isToday && !isSelected) Modifier.border(
                                                2.dp, Indigo500, RoundedCornerShape(10.dp)
                                            ) else Modifier
                                        )
                                        .clickable {
                                            selectedDate = cal
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$dayNum",
                                        fontSize = 13.sp,
                                        fontWeight = if (isToday || isSelected) FontWeight.W700 else FontWeight.W400,
                                        color = if (isSelected) Color.White else colors.textPrimary
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("0%", fontSize = 10.sp, color = colors.textMuted)
                    listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { v ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(getHeatColor(v))
                        )
                    }
                    Text("100%", fontSize = 10.sp, color = colors.textMuted)
                }
            }
        }
        selectedDate?.let { sel ->
            val selectedKey = sdf.format(sel.time)
            val selComps = viewModel.completions[selectedKey] ?: emptyMap()
            val dayFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "📋 ${dayFormat.format(sel.time)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W700,
                        color = colors.textPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (viewModel.habits.isEmpty()) {
                        Text("No habits to show.", fontSize = 13.sp, color = colors.textMuted)
                    } else {
                        viewModel.habits.forEach { habit ->
                            val isCompleted = selComps.containsKey(habit.id)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isCompleted) Green500.copy(alpha = 0.08f) else colors.bgSecondary
                                    )
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(habit.icon, fontSize = 20.sp)
                                Text(
                                    habit.name, fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = colors.textPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(if (isCompleted) "✅" else "❌", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
