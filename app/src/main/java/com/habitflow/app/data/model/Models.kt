package com.habitflow.newapp.data.model

data class Habit(
    val id: String = "",
    val name: String = "",
    val icon: String = "🎯",
    val color: String = "#6366f1",
    val priority: String = "Medium",
    val schedule: String = "Daily",
    val reminder: String = "08:00",
    val notes: String = "",
    val position: Int = 0,
    val createdAt: String = ""
)

data class Streak(
    val current: Int = 0,
    val longest: Int = 0
)

data class UserProfile(
    val xp: Int = 0,
    val createdAt: String = ""
)

data class LeaderboardUser(
    val id: String,
    val name: String,
    val avatar: String,
    val xp: Int,
    val streak: Int,
    val completionRate: Int,
    val level: Int,
    val badges: List<String>,
    val isUser: Boolean = false
)

data class Friend(
    val id: String,
    val name: String,
    val avatar: String,
    val status: String,
    val streak: Int
)

data class Challenge(
    val id: String,
    val name: String,
    val icon: String,
    val participants: Int,
    val daysLeft: Int,
    val progress: Int
)

data class Achievement(
    val id: String,
    val name: String,
    val icon: String,
    val description: String,
    val xp: Int
)

data class Quote(
    val text: String,
    val author: String
)

data class InsightCard(
    val icon: String,
    val title: String,
    val desc: String,
    val color: Long
)
