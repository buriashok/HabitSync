package com.habitflow.newapp.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.local.*
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel

data class SettingsItem(
    val icon: ImageVector,
    val label: String,
    val desc: String,
    val onClick: () -> Unit = {}
)

@Composable
fun ProfileScreen(
    viewModel: HabitViewModel,
    onSignOut: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val colors = AppTheme.colors
    val context = LocalContext.current
    val streak = viewModel.getOverallStreak()
    val level = calculateLevel(viewModel.xp)
    val progress = xpProgress(viewModel.xp)
    val nextLvlXP = xpForNextLevel(viewModel.xp)

    val settingsItems = listOf(
        SettingsItem(Icons.Outlined.Notifications, "Notifications", "Reminders & alerts"),
        SettingsItem(Icons.Outlined.Palette, "Appearance", "Theme & display"),
        SettingsItem(
            Icons.Outlined.Shield, 
            "Privacy", 
            "Data & security",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://your-privacy-policy-url.com"))
                context.startActivity(intent)
            }
        ),
        SettingsItem(Icons.Outlined.Download, "Export Data", "PDF or CSV"),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text("Profile", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
            Text("Your habit journey at a glance", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp))
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Indigo500, Purple500))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("😎", fontSize = 32.sp)
                    }

                    Column {
                        Text(
                            viewModel.currentUser?.displayName ?: viewModel.currentUser?.email?.substringBefore("@") ?: "User",
                            fontSize = 22.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(Icons.Outlined.Email, null, modifier = Modifier.size(13.dp), tint = colors.textSecondary)
                            Text(viewModel.currentUser?.email ?: "", fontSize = 13.sp, color = colors.textSecondary)
                        }

                        Text(
                            "Member since Jan 2026 · Level $level · ${"%,d".format(viewModel.xp)} XP",
                            fontSize = 11.sp,
                            color = colors.textMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Level $level", fontSize = 11.sp, color = colors.textMuted)
                                Text("Level ${level + 1}", fontSize = 11.sp, color = colors.textMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(colors.borderColor)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(progress / 100f)
                                        .background(
                                            Brush.horizontalGradient(listOf(Indigo500, Purple500)),
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        Triple("${streak.current}", "Current\nStreak", Orange500),
                        Triple("${streak.longest}", "Longest\nStreak", Red500),
                        Triple("${viewModel.habits.size}", "Active\nHabits", Green500),
                    ).forEach { (value, label, color) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(value, fontSize = 28.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
                            Text(label, fontSize = 11.sp, color = colors.textMuted, textAlign = TextAlign.Center, lineHeight = 14.sp)
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("🏆 Achievements", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                ACHIEVEMENTS.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEachIndexed { i, badge ->
                            val isUnlocked = i < 2 
                            Column(
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                                    .background(if (isUnlocked) colors.bgSecondary else colors.bgSecondary.copy(alpha = 0.5f))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(badge.icon, fontSize = 24.sp)
                                Text(badge.name, fontSize = 11.sp, fontWeight = FontWeight.W700, color = if (isUnlocked) colors.textPrimary else colors.textMuted, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }

        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("🚀 More", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    Triple(Icons.Outlined.EmojiEvents, "Leaderboard" to "Compete & climb the ranks", "leaderboard"),
                    Triple(Icons.Outlined.SmartToy, "AI Coach" to "Personal habit coaching", "ai"),
                    Triple(Icons.Outlined.CalendarMonth, "Calendar" to "Monthly habit overview", "calendar"),
                ).forEachIndexed { i, (icon, labelDesc, route) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onNavigate(route) }.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(icon, null, tint = Indigo500, modifier = Modifier.size(18.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(labelDesc.first, fontSize = 14.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                            Text(labelDesc.second, fontSize = 11.sp, color = colors.textMuted)
                        }
                        Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(16.dp), tint = colors.textMuted)
                    }
                    if (i < 2) HorizontalDivider(color = colors.borderColor)
                }
            }
        }


        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("⚙️ Settings", fontSize = 15.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                settingsItems.forEachIndexed { i, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { item.onClick() }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(item.icon, null, tint = Indigo500, modifier = Modifier.size(18.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.label, fontSize = 14.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                            Text(item.desc, fontSize = 11.sp, color = colors.textMuted)
                        }
                        Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(16.dp), tint = colors.textMuted)
                    }

                    if (i < settingsItems.size - 1) {
                        HorizontalDivider(color = colors.borderColor)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.logout()
                        onSignOut()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Icon(Icons.Default.Logout, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sign Out", fontWeight = FontWeight.W600)
                }
            }
        }
    }
}
