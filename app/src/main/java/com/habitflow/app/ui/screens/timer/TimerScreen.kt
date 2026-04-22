package com.habitflow.newapp.ui.screens.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.theme.*
import kotlinx.coroutines.delay

data class TimerPreset(
    val label: String,
    val duration: Int,
    val color: Color,
    val icon: String
)

val PRESETS = listOf(
    TimerPreset("Focus", 25 * 60, Indigo500, "🎯"),
    TimerPreset("Short Break", 5 * 60, Green500, "☕"),
    TimerPreset("Long Break", 15 * 60, Cyan500, "🌊"),
    TimerPreset("Custom", 0, Yellow500, "⏱️"),
)

@Composable
fun TimerScreen() {
    val colors = AppTheme.colors
    var modeIndex by remember { mutableIntStateOf(0) }
    var customMinutes by remember { mutableIntStateOf(10) }
    var timeLeft by remember { mutableIntStateOf(PRESETS[0].duration) }
    var isRunning by remember { mutableStateOf(false) }
    var sessions by remember { mutableIntStateOf(0) }

    val currentPreset = PRESETS[modeIndex]
    val duration = if (modeIndex == 3) customMinutes * 60 else currentPreset.duration
    val color = currentPreset.color
    val progress = if (duration > 0) ((duration - timeLeft).toFloat() / duration * 100f) else 0f
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else if (timeLeft == 0 && isRunning) {
            isRunning = false
            if (modeIndex == 0 || modeIndex == 3) {
                sessions++
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Focus Timer", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
                Text("Pomodoro technique for deep work", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PRESETS.forEachIndexed { i, preset ->
                    val isActive = modeIndex == i
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .then(
                                if (isActive) Modifier
                                    .border(2.dp, preset.color, RoundedCornerShape(12.dp))
                                    .background(preset.color.copy(alpha = 0.1f))
                                else Modifier
                                    .border(2.dp, colors.borderColor, RoundedCornerShape(12.dp))
                            )
                            .clickable {
                                modeIndex = i
                                timeLeft = if (i == 3) customMinutes * 60 else PRESETS[i].duration
                                isRunning = false
                            }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(preset.icon, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            preset.label,
                            fontSize = 10.sp,
                            fontWeight = if (isActive) FontWeight.W700 else FontWeight.W500,
                            color = if (isActive) preset.color else colors.textSecondary
                        )
                    }
                }
            }
        }

        if (modeIndex == 3) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⏱️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Set Duration", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                        Spacer(modifier = Modifier.width(12.dp))

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.bgSecondary)
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    val newVal = (customMinutes - 5).coerceIn(1, 120)
                                    customMinutes = newVal
                                    if (!isRunning) timeLeft = newVal * 60
                                },
                                modifier = Modifier.size(36.dp),
                                enabled = !isRunning
                            ) {
                                Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                "$customMinutes",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W800,
                                color = Yellow500,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    val newVal = (customMinutes + 5).coerceIn(1, 120)
                                    customMinutes = newVal
                                    if (!isRunning) timeLeft = newVal * 60
                                },
                                modifier = Modifier.size(36.dp),
                                enabled = !isRunning
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("min", fontSize = 12.sp, color = colors.textMuted)
                    }
                }
            }
        }
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(220.dp)) {
                        val strokeW = 8.dp.toPx()
                        val arcSize = Size(size.width - strokeW, size.height - strokeW)
                        val topLeft = Offset(strokeW / 2, strokeW / 2)

                        drawArc(
                            color = colors.borderColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        drawArc(
                            brush = Brush.sweepGradient(listOf(color, color.copy(alpha = 0.6f), color)),
                            startAngle = -90f,
                            sweepAngle = 360f * progress / 100f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${"%02d".format(minutes)}:${"%02d".format(seconds)}",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.W900,
                            color = color,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "${currentPreset.label} · ${duration / 60} min",
                            fontSize = 11.sp,
                            color = colors.textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {
                            timeLeft = duration
                            isRunning = false
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.bgSecondary)
                    ) {
                        Icon(Icons.Default.Refresh, null, tint = colors.textSecondary, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(color, color.copy(alpha = 0.8f)))
                            )
                            .clickable { isRunning = !isRunning },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Spacer(modifier = Modifier.size(52.dp))
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    Triple("$sessions", "Sessions", Indigo500),
                    Triple("${sessions * 25}", "Minutes", Green500),
                    Triple("${sessions * 50}", "XP Earned", Yellow500),
                ).forEach { (value, label, c) ->
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(value, fontSize = 22.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
                            Text(label, fontSize = 10.sp, color = colors.textMuted)
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("💡 Pro Tips", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    "Work in 25-minute focus blocks followed by 5-minute breaks",
                    "Take a longer 15-minute break after 4 sessions",
                    "Use the custom timer for tasks that need a specific duration",
                    "Each completed session earns you 50 XP"
                ).forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("•", color = colors.textSecondary)
                        Text(tip, fontSize = 13.sp, color = colors.textSecondary, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}
