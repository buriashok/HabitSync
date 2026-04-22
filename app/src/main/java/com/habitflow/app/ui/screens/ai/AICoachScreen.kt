package com.habitflow.newapp.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.local.*
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: Long,
    val type: String, // "ai" or "user"
    val text: String,
    val time: String
)

@Composable
fun AICoachScreen() {
    val colors = AppTheme.colors
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(1, "ai", "Hey there! 👋 I'm your AI Habit Coach. I can help you build better habits, stay motivated, and reach your goals. Ask me anything!", "10:00 AM"),
                ChatMessage(2, "ai", "Here's a quick tip: Research shows that habit stacking — connecting a new habit to an existing one — increases success rates by 65%. Try it out! 🧠", "10:01 AM"),
            )
        )
    }
    var input by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val quickActions = listOf("📊 Weekly Summary", "💪 Motivation Boost", "💡 Habit Tips", "🔥 Streak Analysis")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text("AI Coach", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
            Text("Your personal habit coaching assistant", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(INSIGHT_CARDS) { card ->
                Box(
                    modifier = Modifier
                        .width(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.bgCard)
                        .border(1.dp, colors.borderColor, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(card.color).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(card.icon, fontSize = 16.sp)
                        }
                        Column {
                            Text(card.title, fontSize = 12.sp, fontWeight = FontWeight.W700, color = colors.textPrimary)
                            Text(card.desc, fontSize = 11.sp, color = colors.textMuted, lineHeight = 14.sp)
                        }
                    }
                }
            }
        }
        GlassCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg ->
                    val isAi = msg.type == "ai"
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isAi) Alignment.Start else Alignment.End
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 280.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .then(
                                    if (isAi) {
                                        Modifier.background(colors.bgSecondary)
                                    } else {
                                        Modifier.background(
                                            Brush.linearGradient(listOf(Indigo500, Purple500))
                                        )
                                    }
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                if (isAi) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Icon(Icons.Default.SmartToy, null, tint = Indigo500, modifier = Modifier.size(12.dp))
                                        Text("AI Coach", fontSize = 10.sp, fontWeight = FontWeight.W600, color = Indigo500)
                                    }
                                }
                                Text(
                                    msg.text,
                                    fontSize = 13.sp,
                                    color = if (isAi) colors.textPrimary else Color.White,
                                    lineHeight = 18.sp
                                )
                                Text(
                                    msg.time,
                                    fontSize = 9.sp,
                                    color = if (isAi) colors.textMuted else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
                if (isTyping) {
                    item {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(colors.bgSecondary)
                                .padding(12.dp, 10.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(colors.textMuted)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick actions
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(quickActions) { action ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(colors.bgSecondary)
                            .clickable { input = action.drop(2).trim() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(action, fontSize = 11.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Ask your AI coach...", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                )
                FloatingActionButton(
                    onClick = {
                        if (input.isBlank()) return@FloatingActionButton
                        val userMsg = ChatMessage(
                            System.currentTimeMillis(), "user", input.trim(),
                            timeFormat.format(Date())
                        )
                        messages = messages + userMsg
                        input = ""
                        isTyping = true
                        scope.launch {
                            listState.animateScrollToItem(messages.size)
                            delay(1000 + (Math.random() * 1500).toLong())
                            val response = AI_RESPONSES[(Math.random() * AI_RESPONSES.size).toInt()]
                            val aiMsg = ChatMessage(
                                System.currentTimeMillis() + 1, "ai", response,
                                timeFormat.format(Date())
                            )
                            messages = messages + aiMsg
                            isTyping = false
                            listState.animateScrollToItem(messages.size)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    containerColor = Indigo500,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
