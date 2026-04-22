package com.habitflow.newapp.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.navigation.Screen
import com.habitflow.newapp.navigation.navItems
import com.habitflow.newapp.ui.theme.*

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
) {
    val toggleTheme = LocalToggleTheme.current
    val isDark = LocalIsDarkTheme.current
    val colors = AppTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.3f)
                )
                .background(
                    color = colors.bgNav.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(1.dp, colors.borderColor, RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            navItems.forEach { screen ->
                val isActive = currentRoute == screen.route
                val icon = getNavIcon(screen)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            if (isActive) {
                                Modifier.background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Indigo500, Purple500, Purple400)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            } else Modifier
                        )
                        .clickable { onNavigate(screen.route) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.label,
                            tint = if (isActive) Color.White else colors.textMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        if (isActive) {
                            Text(
                                text = screen.label,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.W600,
                            )
                        }
                    }
                }
            }
            IconButton(
                onClick = toggleTheme,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (isDark) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = "Toggle theme",
                    tint = colors.textMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun getNavIcon(screen: Screen): ImageVector {
    return when (screen) {
        Screen.Dashboard -> Icons.Outlined.Dashboard
        Screen.Habits -> Icons.Outlined.Checklist
        Screen.Analytics -> Icons.Outlined.BarChart
        Screen.Leaderboard -> Icons.Outlined.EmojiEvents
        Screen.AICoach -> Icons.Outlined.SmartToy
        Screen.Calendar -> Icons.Outlined.CalendarMonth
        Screen.Timer -> Icons.Outlined.Timer
        Screen.Profile -> Icons.Outlined.Person
        else -> Icons.Outlined.Home
    }
}
