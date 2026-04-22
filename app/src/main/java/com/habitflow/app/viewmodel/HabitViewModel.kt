package com.habitflow.newapp.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.habitflow.newapp.data.firebase.FirebaseRepository
import com.habitflow.newapp.data.model.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HabitViewModel : ViewModel() {

    private val repo = FirebaseRepository()

    // Auth state
    var currentUser by mutableStateOf<FirebaseUser?>(repo.currentUser)
        private set

    var isLoggedIn by mutableStateOf(repo.currentUser != null)
        private set

    var authLoading by mutableStateOf(true)
        private set

    var authError by mutableStateOf("")

    // Data
    var habits by mutableStateOf<List<Habit>>(emptyList())
        private set

    var completions by mutableStateOf<Map<String, Map<String, Boolean>>>(emptyMap())
        private set

    var streaks by mutableStateOf<Map<String, Streak>>(emptyMap())
        private set

    var xp by mutableIntStateOf(0)
        private set

    var loaded by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            try {
                repo.authStateFlow().collect { user ->
                    currentUser = user
                    isLoggedIn = user != null
                    authLoading = false

                    if (user != null) {
                        loadUserData(user.uid)
                    } else {
                        clearData()
                    }
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Auth state error", e)
                authLoading = false
            }
        }
    }

    private fun getToday(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    // ===== AUTH =====
    fun signInWithEmail(email: String, password: String) {
        authError = ""
        authLoading = true
        viewModelScope.launch {
            try {
                val result = repo.signInWithEmail(email, password)
                result.onFailure { authError = it.message ?: "Sign in failed" }
            } catch (e: Exception) {
                authError = e.message ?: "Sign in failed"
            }
            authLoading = false
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        authError = ""
        authLoading = true
        viewModelScope.launch {
            try {
                val result = repo.signUpWithEmail(email, password)
                result.onFailure { authError = it.message ?: "Sign up failed" }
            } catch (e: Exception) {
                authError = e.message ?: "Sign up failed"
            }
            authLoading = false
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        authError = ""
        authLoading = true
        viewModelScope.launch {
            try {
                val result = repo.signInWithGoogleToken(idToken)
                result.onFailure { authError = it.message ?: "Google sign in failed" }
            } catch (e: Exception) {
                authError = e.message ?: "Google sign in failed"
            }
            authLoading = false
        }
    }

    fun logout() {
        repo.signOut()
    }

    // ===== DATA =====
    private fun loadUserData(uid: String) {
        loaded = false

        // Listen habits
        viewModelScope.launch {
            try {
                repo.habitsFlow(uid).collect { h ->
                    habits = h
                    loaded = true
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Habits load error", e)
                loaded = true
            }
        }

        // Listen completions
        viewModelScope.launch {
            try {
                repo.completionsFlow(uid).collect { c ->
                    completions = c
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Completions load error", e)
            }
        }

        // Listen XP
        viewModelScope.launch {
            try {
                repo.xpFlow(uid).collect { x ->
                    xp = x
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "XP load error", e)
            }
        }

        // Listen streaks
        viewModelScope.launch {
            try {
                repo.streaksFlow(uid).collect { s ->
                    streaks = s
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Streaks load error", e)
            }
        }
    }

    private fun clearData() {
        habits = emptyList()
        completions = emptyMap()
        streaks = emptyMap()
        xp = 0
        loaded = false
    }

    // ===== ACTIONS =====
    fun toggleHabit(habitId: String) {
        val uid = currentUser?.uid ?: return
        val today = getToday()
        val todayComps = completions[today]?.toMutableMap() ?: mutableMapOf()
        val wasCompleted = todayComps.containsKey(habitId)

        // Update local state immediately
        if (wasCompleted) {
            todayComps.remove(habitId)
            xp = maxOf(0, xp - 25)
        } else {
            todayComps[habitId] = true
            xp += 25
        }
        completions = completions.toMutableMap().apply { put(today, todayComps) }

        val habitStreak = streaks[habitId] ?: Streak(0, 0)
        val newCurrent = if (!wasCompleted) habitStreak.current + 1 else maxOf(0, habitStreak.current - 1)
        val newLongest = maxOf(habitStreak.longest, newCurrent)
        streaks = streaks.toMutableMap().apply { put(habitId, Streak(newCurrent, newLongest)) }

        // Sync to Firestore in background
        viewModelScope.launch {
            try {
                repo.setCompletion(uid, today, habitId, !wasCompleted)
                repo.updateXp(uid, xp)
                repo.updateStreaks(uid, streaks)
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Toggle sync error", e)
            }
        }
    }

    fun addHabit(habit: Habit) {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                repo.addHabit(uid, habit.copy(position = habits.size, createdAt = getToday()))
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Add habit error", e)
            }
        }
    }

    fun updateHabit(id: String, updatedHabit: Habit) {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                repo.updateHabit(uid, id, updatedHabit)
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Update habit error", e)
            }
        }
    }

    fun deleteHabit(id: String) {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                repo.deleteHabit(uid, id)
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Delete habit error", e)
            }
        }
    }

    // ===== COMPUTED =====
    fun getTodayCompletions(): Map<String, Boolean> {
        return completions[getToday()] ?: emptyMap()
    }

    fun getTodayProgress(): Int {
        if (habits.isEmpty()) return 0
        val completed = (completions[getToday()] ?: emptyMap()).size
        return ((completed.toFloat() / habits.size.toFloat()) * 100).toInt()
    }

    fun getOverallStreak(): Streak {
        val streakVals = streaks.values.toList()
        if (streakVals.isEmpty()) return Streak(0, 0)
        return Streak(
            current = streakVals.maxOf { it.current },
            longest = streakVals.maxOf { it.longest }
        )
    }
}
