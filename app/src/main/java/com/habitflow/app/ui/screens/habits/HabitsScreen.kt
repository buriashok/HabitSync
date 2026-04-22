package com.habitflow.newapp.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.newapp.data.local.*
import com.habitflow.newapp.data.model.Habit
import com.habitflow.newapp.ui.components.GlassCard
import com.habitflow.newapp.ui.components.parseHabitColor
import com.habitflow.newapp.ui.theme.*
import com.habitflow.newapp.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(viewModel: HabitViewModel) {
    val colors = AppTheme.colors
    var search by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<Habit?>(null) }

    val filtered = viewModel.habits.filter {
        it.name.contains(search, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            item {
                Text("Habits", fontSize = 32.sp, fontWeight = FontWeight.W800, color = colors.textPrimary)
                Text("Manage your daily habits", fontSize = 14.sp, color = colors.textSecondary, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
            }

            // Search
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Search habits...", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Search, null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                )
            }

            // Habit List
            items(filtered) { habit ->
                val habitColor = parseHabitColor(habit.color)

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(habitColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(habit.icon, fontSize = 22.sp)
                        }

                        // Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(habit.name, fontSize = 15.sp, fontWeight = FontWeight.W600, color = colors.textPrimary,
                                maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                val priorityColor = when (habit.priority) {
                                    "High" -> Orange500
                                    "Critical" -> Red500
                                    "Low" -> Green500
                                    else -> Indigo500
                                }
                                Text(
                                    habit.priority,
                                    fontSize = 10.sp, fontWeight = FontWeight.W600,
                                    color = priorityColor,
                                    modifier = Modifier
                                        .background(priorityColor.copy(alpha = 0.1f), RoundedCornerShape(100))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                                Text(
                                    habit.schedule,
                                    fontSize = 10.sp, fontWeight = FontWeight.W600,
                                    color = Indigo500,
                                    modifier = Modifier
                                        .background(Indigo500.copy(alpha = 0.1f), RoundedCornerShape(100))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                                if (habit.reminder.isNotEmpty()) {
                                    Text(
                                        "⏰ ${habit.reminder}",
                                        fontSize = 10.sp, fontWeight = FontWeight.W600,
                                        color = colors.textMuted,
                                        modifier = Modifier
                                            .background(colors.bgSecondary, RoundedCornerShape(100))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Actions
                        IconButton(onClick = {
                            editingHabit = habit
                            showDialog = true
                        }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(16.dp), tint = colors.textMuted)
                        }
                        IconButton(onClick = {
                            viewModel.deleteHabit(habit.id)
                        }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Outlined.Delete, null, modifier = Modifier.size(16.dp), tint = Red500)
                        }
                    }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = {
                editingHabit = null
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = Indigo500,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, "Add Habit")
        }

        // Add/Edit Dialog
        if (showDialog) {
            HabitDialog(
                habit = editingHabit,
                onSave = { habit ->
                    if (editingHabit != null) {
                        viewModel.updateHabit(editingHabit!!.id, habit)
                    } else {
                        viewModel.addHabit(habit)
                    }
                    showDialog = false
                    editingHabit = null
                },
                onDismiss = {
                    showDialog = false
                    editingHabit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDialog(
    habit: Habit?,
    onSave: (Habit) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = AppTheme.colors
    var name by remember { mutableStateOf(habit?.name ?: "") }
    var icon by remember { mutableStateOf(habit?.icon ?: "🎯") }
    var color by remember { mutableStateOf(habit?.color ?: "#6366f1") }
    var priority by remember { mutableStateOf(habit?.priority ?: "Medium") }
    var schedule by remember { mutableStateOf(habit?.schedule ?: "Daily") }
    var notes by remember { mutableStateOf(habit?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onSave(Habit(
                        id = habit?.id ?: "",
                        name = name,
                        icon = icon,
                        color = color,
                        priority = priority,
                        schedule = schedule,
                        notes = notes,
                        position = habit?.position ?: 0,
                        createdAt = habit?.createdAt ?: ""
                    ))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Indigo500)
            ) {
                Text(if (habit != null) "Update" else "Add Habit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(if (habit != null) "Edit Habit" else "New Habit", fontWeight = FontWeight.W700)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )

                // Icon picker
                Text("Icon", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(HABIT_ICONS) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .then(
                                    if (emoji == icon) Modifier
                                        .background(Indigo500.copy(alpha = 0.15f))
                                        .border(2.dp, Indigo500, RoundedCornerShape(10.dp))
                                    else Modifier
                                        .background(colors.bgSecondary)
                                )
                                .clickable { icon = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 18.sp)
                        }
                    }
                }

                // Color picker
                Text("Color", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(HABIT_COLORS) { hex ->
                        val c = parseHabitColor(hex)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(c)
                                .then(
                                    if (hex == color) Modifier.border(3.dp, Color.White, CircleShape)
                                    else Modifier
                                )
                                .clickable { color = hex }
                        )
                    }
                }

                // Priority
                Text("Priority", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PRIORITIES.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p, fontSize = 12.sp) },
                            shape = RoundedCornerShape(100),
                        )
                    }
                }

                // Schedule
                Text("Schedule", fontSize = 13.sp, fontWeight = FontWeight.W600, color = colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SCHEDULES.forEach { s ->
                        FilterChip(
                            selected = schedule == s,
                            onClick = { schedule = s },
                            label = { Text(s, fontSize = 12.sp) },
                            shape = RoundedCornerShape(100),
                        )
                    }
                }

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                )
            }
        }
    )
}
