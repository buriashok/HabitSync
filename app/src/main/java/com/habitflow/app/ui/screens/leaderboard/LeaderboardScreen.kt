package com.habitflow.newapp.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.theme.*

@Composable
fun LeaderboardScreen() {
    val colors = AppTheme.colors
    var tab by remember { mutableStateOf("global") }
    var sortBy by remember { mutableStateOf("xp") }

    val sorted = remember(sortBy) {
        MOCK_LEADERBOARD.sortedByDescending { u ->
            when (sortBy) {
                "xp" -> u.xp
                "streak" -> u.streak
                else -> u.completionRate
            }
        }
    }

    val tabs = listOf(
        "global" to "🏆 Global",
        "friends" to "👥 Friends",
        "challenges" to "⚔️ Challenges",
        "badges" to "🏅 Badges"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header
        item {
            Text("Leaderboard", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
            Text("Compete with friends and climb the ranks", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp, bottom = 4.dp))
        }

        // Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.bgSecondary)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tabs.forEach { (id, label) ->
                    val isActive = tab == id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .then(
                                if (isActive) Modifier.background(Indigo500)
                                else Modifier
                            )
                            .clickable { tab = id }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.W600 else FontWeight.W500,
                            color = if (isActive) Color.White else colors.textSecondary
                        )
                    }
                }
            }
        }

        when (tab) {
            "global" -> {
                // Sort options
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("xp" to "⚡ XP", "streak" to "🔥 Streak", "rate" to "🏆 Rate").forEach { (id, label) ->
                            val isActive = sortBy == id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .then(
                                        if (isActive) Modifier.background(Indigo500) else Modifier.background(colors.bgSecondary)
                                    )
                                    .clickable { sortBy = id }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(label, fontSize = 12.sp, fontWeight = FontWeight.W600,
                                    color = if (isActive) Color.White else colors.textSecondary)
                            }
                        }
                    }
                }

                // Podium
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (sorted.size >= 3) {
                                val order = listOf(sorted[1], sorted[0], sorted[2])
                                val heights = listOf(90, 120, 70)
                                val ranks = listOf(2, 1, 3)
                                order.forEachIndexed { i, user ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(user.avatar, fontSize = 28.sp)
                                        Text(user.name.split(' ')[0], fontSize = 12.sp, fontWeight = FontWeight.W700, color = colors.textPrimary,
                                            modifier = Modifier.padding(vertical = 4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(64.dp)
                                                .height(heights[i].dp)
                                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                                .background(
                                                    when (ranks[i]) {
                                                        1 -> Brush.linearGradient(listOf(Indigo500, Purple500))
                                                        2 -> Brush.linearGradient(listOf(Color(0xFF94A3B8), Color(0xFFCBD5E1)))
                                                        else -> Brush.linearGradient(listOf(Orange500, Color(0xFFFB923C)))
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.EmojiEvents, null, tint = Color.White, modifier = Modifier.size(if (ranks[i] == 1) 24.dp else 16.dp))
                                                Text("#${ranks[i]}", color = Color.White, fontWeight = FontWeight.W800, fontSize = 14.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Rankings
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        sorted.forEachIndexed { i, user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (user.isUser) Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Indigo500.copy(alpha = 0.1f))
                                            .border(1.dp, Indigo500.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                        else Modifier
                                    )
                                    .padding(vertical = 10.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Rank
                                val rankColor = when (i) {
                                    0 -> Indigo500
                                    1 -> Color(0xFF94A3B8)
                                    2 -> Orange500
                                    else -> colors.bgSecondary
                                }
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(rankColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${i + 1}", fontSize = 12.sp, fontWeight = FontWeight.W700,
                                        color = if (i < 3) Color.White else colors.textSecondary)
                                }
                                Text(user.avatar, fontSize = 24.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${user.name}${if (user.isUser) " (You)" else ""}", fontSize = 14.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                                    Text("🔥 ${user.streak}d · ✅ ${user.completionRate}% · Lvl ${user.level}",
                                        fontSize = 11.sp, color = colors.textMuted)
                                }
                                Text("${"%,d".format(user.xp)} XP", fontSize = 13.sp, fontWeight = FontWeight.W700, color = Indigo500)
                            }
                        }
                    }
                }
            }

            "friends" -> {
                item {
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo500)
                    ) {
                        Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Friend")
                    }
                }
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        MOCK_FRIENDS.forEach { friend ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(friend.avatar, fontSize = 26.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(friend.name, fontSize = 14.sp, fontWeight = FontWeight.W600, color = colors.textPrimary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val statusColor = if (friend.status == "online") Green500 else colors.textMuted
                                        Text("● ${friend.status}", fontSize = 11.sp, color = statusColor)
                                        Text("🔥 ${friend.streak}d streak", fontSize = 11.sp, color = colors.textMuted)
                                    }
                                }
                                OutlinedButton(
                                    onClick = {},
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Challenge", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            "challenges" -> {
                items(MOCK_CHALLENGES) { ch ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(ch.icon, fontSize = 32.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ch.name, fontSize = 14.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 4.dp)) {
                                    Text("👥 ${ch.participants} joined", fontSize = 11.sp, color = colors.textMuted)
                                    Text("⏰ ${ch.daysLeft} days left", fontSize = 11.sp, color = colors.textMuted)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(colors.borderColor)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(ch.progress / 100f)
                                            .background(
                                                Brush.horizontalGradient(listOf(Indigo500, Purple500)),
                                                RoundedCornerShape(3.dp)
                                            )
                                    )
                                }
                                Text("${ch.progress}% complete", fontSize = 10.sp, color = colors.textMuted, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }

            "badges" -> {
                item {
                    val unlockedIds = listOf("7_day_warrior", "early_bird", "perfect_week")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ACHIEVEMENTS.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { badge ->
                                    val isUnlocked = badge.id in unlockedIds
                                    GlassCard(modifier = Modifier.weight(1f)) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .then(if (!isUnlocked) Modifier else Modifier)
                                        ) {
                                            Text(
                                                badge.icon,
                                                fontSize = 28.sp,
                                                modifier = if (!isUnlocked) Modifier else Modifier
                                            )
                                            Text(
                                                badge.name, fontSize = 12.sp,
                                                fontWeight = FontWeight.W700,
                                                color = if (isUnlocked) colors.textPrimary else colors.textMuted,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                            Text(
                                                badge.description, fontSize = 10.sp,
                                                color = colors.textMuted,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                            Text(
                                                "+${badge.xp} XP", fontSize = 10.sp,
                                                fontWeight = FontWeight.W700,
                                                color = if (isUnlocked) Indigo500 else colors.textMuted,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                                if (row.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
