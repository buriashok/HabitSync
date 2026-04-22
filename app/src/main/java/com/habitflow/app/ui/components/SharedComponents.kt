package com.habitflow.newapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.model.Habit
import com.habitflow.newapp.data.model.Streak
import com.habitflow.newapp.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.colors
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.bgCard.copy(alpha = 0.85f)
        ),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun StatCard(
    icon: @Composable () -> Unit,
    iconBgColor: Color,
    value: Int,
    label: String,
    suffix: String = "",
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    GlassCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBgColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column {
                Text(
                    text = "$value$suffix",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W800,
                    color = colors.textPrimary,
                    letterSpacing = (-0.02).sp
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressRing(
    progress: Int,
    size: Dp = 130.dp,
    strokeWidth: Dp = 10.dp,
) {
    val colors = AppTheme.colors
    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val sweepAngle = 360f * animatedProgress
            val stroke = strokeWidth.toPx()
            val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)
            drawArc(
                color = colors.borderColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Indigo500, Purple500, Pink500, Indigo500)
                ),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${progress}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.W800,
                color = Indigo500,
            )
            Text(
                text = "Complete",
                fontSize = 10.sp,
                color = colors.textMuted,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.W500
            )
        }
    }
}
@Composable
fun HabitItemRow(
    habit: Habit,
    isCompleted: Boolean,
    streak: Streak?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val habitColor = parseHabitColor(habit.color)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.bgCard)
            .border(1.dp, colors.borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp, 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .then(
                    if (isCompleted) {
                        Modifier.background(
                            Brush.linearGradient(listOf(habitColor, habitColor.copy(alpha = 0.8f))),
                            RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier.border(2.dp, habitColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(text = habit.icon, fontSize = 24.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = if (isCompleted) colors.textMuted else colors.textPrimary,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = habit.schedule,
                    fontSize = 11.sp,
                    color = colors.textMuted
                )
                if (streak != null && streak.current > 0) {
                    Text(
                        text = "🔥 ${streak.current}d",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W600,
                        color = Orange500,
                        modifier = Modifier
                            .background(
                                Orange500.copy(alpha = 0.1f),
                                RoundedCornerShape(100)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                val priorityColor = when (habit.priority) {
                    "High" -> Orange500
                    "Critical" -> Red500
                    else -> Indigo500
                }
                Text(
                    text = habit.priority,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W600,
                    color = priorityColor,
                    modifier = Modifier
                        .background(
                            priorityColor.copy(alpha = 0.1f),
                            RoundedCornerShape(100)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.borderColor)
            ) {
                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(listOf(habitColor, habitColor.copy(alpha = 0.6f))),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }

        if (isCompleted) {
            Text("✅", fontSize = 18.sp)
        }
    }
}



fun parseHabitColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Indigo500
    }
}
