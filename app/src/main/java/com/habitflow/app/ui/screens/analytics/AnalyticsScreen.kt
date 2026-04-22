package com.habitflow.newapp.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.local.*
import com.habitflow.newapp.ui.components.*
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnalyticsScreen(viewModel: HabitViewModel) {
    val colors = AppTheme.colors
    val todayCompletions = viewModel.getTodayCompletions()
    val habits = viewModel.habits
    val completions = viewModel.completions

    // Calculate analytics data
    val totalDays = completions.size
    val totalCompletions = completions.values.sumOf { it.size }
    val completionRate = if (totalDays > 0 && habits.isNotEmpty())
        (totalCompletions.toFloat() / (totalDays * habits.size).toFloat() * 100).toInt()
    else 0

    val overallStreak = viewModel.getOverallStreak()

    val habitStats = habits.map { habit ->
        val completed = completions.values.count { it.containsKey(habit.id) }
        val rate = if (totalDays > 0) (completed.toFloat() / totalDays * 100).toInt() else 0
        Triple(habit, completed, rate)
    }.sortedByDescending { it.third }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val today = Date()
    val weeklyData = (0..6).map { d ->
        val cal = Calendar.getInstance()
        cal.time = today
        cal.add(Calendar.DAY_OF_YEAR, -d)
        val dayKey = sdf.format(cal.time)
        val dayCompletions = completions[dayKey] ?: emptyMap()
        val dayFormat = SimpleDateFormat("EEE", Locale.US)
        Pair(dayFormat.format(cal.time), dayCompletions.size)
    }.reversed()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Text("Analytics", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
            Text("Your habit performance insights", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp, bottom = 4.dp))
        }

        // Stats overview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = { Text("📊", fontSize = 20.sp) },
                    iconBgColor = Green500,
                    value = completionRate,
                    label = "Completion",
                    suffix = "%",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = { Text("🔥", fontSize = 20.sp) },
                    iconBgColor = Orange500,
                    value = overallStreak.current,
                    label = "Streak",
                    suffix = "d",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = { Text("📅", fontSize = 20.sp) },
                    iconBgColor = Blue500,
                    value = totalDays,
                    label = "Days Tracked",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = { Text("✅", fontSize = 20.sp) },
                    iconBgColor = Purple500,
                    value = totalCompletions,
                    label = "Completions",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            if (habitStats.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val best = habitStats.first()
                    val worst = habitStats.last()

                    GlassCard(modifier = Modifier.weight(1f)) {
                        Text("🏆 Best Habit", fontSize = 12.sp, fontWeight = FontWeight.W600, color = Green500)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(best.first.icon, fontSize = 20.sp)
                            Column {
                                Text(best.first.name, fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                                Text("${best.third}% completion", fontSize = 11.sp, color = colors.textMuted)
                            }
                        }
                    }
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Text("📉 Needs Work", fontSize = 12.sp, fontWeight = FontWeight.W600, color = Orange500)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(worst.first.icon, fontSize = 20.sp)
                            Column {
                                Text(worst.first.name, fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                                Text("${worst.third}% completion", fontSize = 11.sp, color = colors.textMuted)
                            }
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("📈 Weekly Overview", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                val maxVal = (weeklyData.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)

                Row(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyData.forEach { (day, count) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.width(36.dp)
                        ) {
                            Text("$count", fontSize = 11.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height((count.toFloat() / maxVal * 80).dp.coerceAtLeast(4.dp))
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        Brush.verticalGradient(listOf(Indigo500, Purple500))
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(day, fontSize = 10.sp, color = colors.textMuted)
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("📋 Per-Habit Breakdown", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                habitStats.forEach { (habit, completed, rate) ->
                    val habitColor = parseHabitColor(habit.color)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(habit.icon, fontSize = 18.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(habit.name, fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                                Text("$rate%", fontSize = 13.sp, fontWeight = FontWeight.W700, color = habitColor)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(colors.borderColor)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(rate / 100f)
                                        .background(habitColor, RoundedCornerShape(3.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("🟩 Consistency Map", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Text("Last 5 weeks", fontSize = 11.sp, color = colors.textMuted, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))

                val gridDays = (0..34).map { d ->
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DAY_OF_YEAR, -d)
                    val key = sdf.format(cal.time)
                    val dayComps = completions[key] ?: emptyMap()
                    val ratio = if (habits.isNotEmpty()) dayComps.size.toFloat() / habits.size else 0f
                    ratio
                }.reversed()

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (row in 0..4) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            for (col in 0..6) {
                                val index = row * 7 + col
                                if (index < gridDays.size) {
                                    val ratio = gridDays[index]
                                    val cellColor = when {
                                        ratio == 0f -> colors.bgSecondary
                                        ratio <= 0.25f -> Indigo500.copy(alpha = 0.15f)
                                        ratio <= 0.5f -> Indigo500.copy(alpha = 0.3f)
                                        ratio <= 0.75f -> Indigo500.copy(alpha = 0.55f)
                                        else -> Indigo500.copy(alpha = 0.85f)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(cellColor)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("0%", fontSize = 10.sp, color = colors.textMuted)
                    listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { v ->
                        val c = when {
                            v == 0f -> colors.bgSecondary
                            v <= 0.25f -> Indigo500.copy(alpha = 0.15f)
                            v <= 0.5f -> Indigo500.copy(alpha = 0.3f)
                            v <= 0.75f -> Indigo500.copy(alpha = 0.55f)
                            else -> Indigo500.copy(alpha = 0.85f)
                        }
                        Box(
                            modifier = Modifier.size(12.dp).clip(RoundedCornerShape(2.dp)).background(c)
                        )
                    }
                    Text("100%", fontSize = 10.sp, color = colors.textMuted)
                }
            }
        }
    }
}
