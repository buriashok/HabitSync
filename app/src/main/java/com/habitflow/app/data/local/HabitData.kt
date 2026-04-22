package com.habitflow.newapp.data.local

import com.habitflow.newapp.data.model.*
import java.util.Calendar

val HABIT_ICONS = listOf("🏃", "📚", "💧", "🧘", "💪", "🎨", "✍️", "🎵", "🍎", "😴", "🧠", "💊", "🌿", "📝", "🎯", "⏰", "🚶", "🏋️", "🧹", "💻")

val HABIT_COLORS = listOf(
    "#6366f1", "#8b5cf6", "#a855f7", "#ec4899", "#f43f5e",
    "#ef4444", "#f97316", "#eab308", "#22c55e", "#14b8a6",
    "#06b6d4", "#3b82f6", "#2563eb", "#7c3aed", "#db2777"
)

val PRIORITIES = listOf("Low", "Medium", "High", "Critical")
val SCHEDULES = listOf("Daily", "Weekdays", "Weekends", "Custom")

val DEFAULT_HABITS = listOf(
    Habit("1", "Morning Meditation", "🧘", "#8b5cf6", "High", "Daily", "06:00", "", 0, "2026-01-01"),
    Habit("2", "Read 30 minutes", "📚", "#3b82f6", "Medium", "Daily", "20:00", "", 1, "2026-01-01"),
    Habit("3", "Exercise", "💪", "#22c55e", "High", "Daily", "07:00", "", 2, "2026-01-01"),
    Habit("4", "Drink 2L Water", "💧", "#06b6d4", "Medium", "Daily", "08:00", "", 3, "2026-01-01"),
    Habit("5", "Journal", "✍️", "#f97316", "Low", "Daily", "21:00", "", 4, "2026-01-01"),
    Habit("6", "Practice Coding", "💻", "#6366f1", "High", "Weekdays", "10:00", "", 5, "2026-01-05"),
)

val QUOTES = listOf(
    Quote("The secret of getting ahead is getting started.", "Mark Twain"),
    Quote("We are what we repeatedly do. Excellence is not an act, but a habit.", "Aristotle"),
    Quote("Success is the sum of small efforts repeated day in and day out.", "Robert Collier"),
    Quote("Motivation is what gets you started. Habit is what keeps you going.", "Jim Rohn"),
    Quote("First forget inspiration. Habit is more dependable.", "Octavia Butler"),
    Quote("Good habits formed at youth make all the difference.", "Aristotle"),
    Quote("Chains of habit are too light to be felt until they are too heavy to be broken.", "Warren Buffett"),
    Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
    Quote("Small daily improvements are the key to staggering long-term results.", "Unknown"),
    Quote("You do not rise to the level of your goals. You fall to the level of your systems.", "James Clear"),
)

val ACHIEVEMENTS = listOf(
    Achievement("early_bird", "Early Bird", "🌅", "Complete a habit before 7 AM", 50),
    Achievement("consistency_king", "Consistency King", "👑", "Maintain a 21-day streak", 500),
    Achievement("7_day_warrior", "7 Day Warrior", "⚔️", "Complete a 7-day streak", 100),
    Achievement("top_1", "Top 1%", "🏆", "Reach the top of the leaderboard", 1000),
    Achievement("perfect_week", "Perfect Week", "✨", "Complete all habits for 7 days", 200),
    Achievement("habit_master", "Habit Master", "🎓", "Create 10 habits", 150),
    Achievement("streak_50", "50 Day Legend", "🔥", "Maintain a 50-day streak", 750),
    Achievement("streak_100", "Centurion", "💯", "Maintain a 100-day streak", 1500),
    Achievement("social_butterfly", "Social Butterfly", "🦋", "Add 5 friends", 100),
    Achievement("challenger", "Challenger", "🥊", "Complete 3 challenges", 200),
)

val MOCK_LEADERBOARD = listOf(
    LeaderboardUser("u1", "Sarah Chen", "👩‍💻", 12450, 87, 96, 24, listOf("consistency_king", "streak_50", "7_day_warrior")),
    LeaderboardUser("u2", "Alex Rivera", "🧑‍🎨", 11200, 72, 94, 22, listOf("consistency_king", "7_day_warrior", "perfect_week")),
    LeaderboardUser("u3", "Jordan Kim", "🧑‍🔬", 9800, 65, 91, 19, listOf("streak_50", "7_day_warrior")),
    LeaderboardUser("u4", "You", "😎", 8500, 42, 88, 17, listOf("consistency_king", "7_day_warrior"), true),
    LeaderboardUser("u5", "Morgan Lee", "🧑‍💼", 7600, 38, 85, 15, listOf("7_day_warrior", "perfect_week")),
    LeaderboardUser("u6", "Taylor Swift", "🎤", 6900, 31, 82, 14, listOf("7_day_warrior")),
    LeaderboardUser("u7", "Chris Evans", "🦸", 5500, 25, 78, 11, listOf("early_bird")),
    LeaderboardUser("u8", "Priya Patel", "👩‍⚕️", 4200, 19, 75, 9, listOf("7_day_warrior")),
)

val MOCK_FRIENDS = listOf(
    Friend("f1", "Sarah Chen", "👩‍💻", "online", 87),
    Friend("f2", "Alex Rivera", "🧑‍🎨", "online", 72),
    Friend("f3", "Morgan Lee", "🧑‍💼", "offline", 38),
)

val MOCK_CHALLENGES = listOf(
    Challenge("c1", "30-Day Fitness Challenge", "💪", 156, 18, 40),
    Challenge("c2", "Reading Marathon", "📚", 89, 12, 60),
    Challenge("c3", "Mindfulness Week", "🧘", 234, 3, 85),
)

val AI_RESPONSES = listOf(
    "Great job staying consistent! Your morning meditation streak is impressive. Try extending your session by 5 minutes this week. 🧘",
    "I notice you've been skipping your reading habit on weekends. Consider setting a more relaxed reading goal for those days. 📚",
    "Your exercise consistency is at 92% this month! You're in the top 10% of users. Keep pushing! 💪",
    "Pro tip: Try habit stacking - attach your new habit to an existing one. For example, journal right after your evening tea. ✍️",
    "Your streak is at risk! You haven't completed your water intake habit today. Remember, staying hydrated improves focus by 25%. 💧",
    "Weekly Summary: You completed 85% of habits this week, up from 78% last week. Your strongest habit is meditation. 🎯",
    "Based on your patterns, you're most productive between 7-9 AM. Consider scheduling your hardest habits during this window. ⏰",
    "Congratulations on your 7-day streak! Research shows it takes 21 days to form a habit. You're 1/3 of the way there! 🔥",
)

val INSIGHT_CARDS = listOf(
    InsightCard("🔥", "Streak Alert", "Your meditation streak is at 14 days! Keep it up for a 21-day badge.", 0xFFF97316),
    InsightCard("📈", "Progress", "Your completion rate improved 12% this week. Great momentum!", 0xFF22C55E),
    InsightCard("💡", "Suggestion", "Try scheduling your hardest habits during your peak energy hours (7-9 AM).", 0xFF6366F1),
    InsightCard("⚠️", "At Risk", "Your reading habit has been inconsistent. Consider reducing the goal to 15 min.", 0xFFF59E0B),
)

fun getQuoteOfDay(): Quote {
    val cal = Calendar.getInstance()
    val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
    return QUOTES[dayOfYear % QUOTES.size]
}

fun calculateLevel(xp: Int): Int = xp / 500 + 1

fun xpForNextLevel(xp: Int): Int = calculateLevel(xp) * 500

fun xpProgress(xp: Int): Float {
    val currentLevelXP = (calculateLevel(xp) - 1) * 500
    val nextLevelXP = calculateLevel(xp) * 500
    return ((xp - currentLevelXP).toFloat() / (nextLevelXP - currentLevelXP).toFloat()) * 100f
}
