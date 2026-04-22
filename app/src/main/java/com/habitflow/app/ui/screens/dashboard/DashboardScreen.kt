package com.habitflow.newapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.local.*
import com.habitflow.newapp.ui.components.*
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(viewModel: HabitViewModel) {
    val colors = AppTheme.colors
    val todayCompletions = viewModel.getTodayCompletions()
    val progress = viewModel.getTodayProgress()
    val overallStreak = viewModel.getOverallStreak()
    val quote = getQuoteOfDay()
    val level = calculateLevel(viewModel.xp)
    val levelProgress = xpProgress(viewModel.xp)
    val completedCount = todayCompletions.size

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    val greetingEmoji = when {
        hour < 12 -> "☀️"
        hour < 17 -> "🌤️"
        else -> "🌙"
    }

    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
    val todayStr = dateFormat.format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(greetingEmoji, fontSize = 24.sp)
                    Text(greeting, fontSize = 14.sp, color = colors.textSecondary, fontWeight = FontWeight.W500)
                }
                Text(
                    "Dashboard",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.W800,
                    color = Indigo500,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(14.dp), tint = colors.textSecondary)
                    Text(todayStr, fontSize = 13.sp, color = colors.textSecondary)
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassCard(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressRing(progress = progress, size = 110.dp, strokeWidth = 8.dp)
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        icon = { Text("🔥", fontSize = 20.sp) },
                        iconBgColor = Orange500,
                        value = overallStreak.current,
                        label = "Day Streak"
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = { Icon(Icons.Default.Bolt, null, tint = Indigo500, modifier = Modifier.size(22.dp)) },
                    iconBgColor = Indigo500,
                    value = viewModel.xp,
                    label = "XP Points",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = { Icon(Icons.Default.Star, null, tint = Yellow500, modifier = Modifier.size(22.dp)) },
                    iconBgColor = Yellow500,
                    value = level,
                    label = "Level",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Level $level", fontSize = 12.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                    Text(
                        "${levelProgress.toInt()}% · ${xpForNextLevel(viewModel.xp) - viewModel.xp} XP to Level ${level + 1}",
                        fontSize = 11.sp, color = colors.textMuted
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(colors.borderColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(levelProgress / 100f)
                            .background(
                                Brush.horizontalGradient(listOf(Indigo500, Purple500, Purple400)),
                                RoundedCornerShape(5.dp)
                            )
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(listOf(Indigo500, Purple500, Purple400))
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                        Text("Daily Inspiration", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.W600)
                    }
                    Text(
                        text = quote.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "— ${quote.author}",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎯", fontSize = 22.sp)
                            Text("Daily Challenge", fontWeight = FontWeight.W700, fontSize = 15.sp, color = colors.textPrimary)
                        }
                        Text(
                            "Complete all ${viewModel.habits.size} habits for 100 bonus XP",
                            fontSize = 13.sp,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$completedCount/${viewModel.habits.size}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W800,
                            color = if (completedCount == viewModel.habits.size) Green500 else Indigo500
                        )
                        Text("habits", fontSize = 10.sp, color = colors.textMuted)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(colors.borderColor)
                ) {
                    val fraction = if (viewModel.habits.isNotEmpty())
                        completedCount.toFloat() / viewModel.habits.size else 0f
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction)
                            .background(
                                if (completedCount == viewModel.habits.size)
                                    Brush.horizontalGradient(listOf(Green500, Green600))
                                else
                                    Brush.horizontalGradient(listOf(Indigo500, Purple500)),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }

        item {
            Text(
                "Today's Habits",
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = colors.textPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        itemsIndexed(viewModel.habits) { index, habit ->
            val isCompleted = todayCompletions.containsKey(habit.id)
            val streak = viewModel.streaks[habit.id]

            HabitItemRow(
                habit = habit,
                isCompleted = isCompleted,
                streak = streak,
                onClick = { viewModel.toggleHabit(habit.id) }
            )
        }

        if (viewModel.habits.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎯", fontSize = 48.sp)
                    Text("No habits yet", fontWeight = FontWeight.W600, color = colors.textMuted, modifier = Modifier.padding(top = 8.dp))
                    Text("Add your first habit to get started!", fontSize = 13.sp, color = colors.textMuted, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
