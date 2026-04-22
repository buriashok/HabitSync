package com.habitflow.newapp.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.habitflow.newapp.data.model.Habit
import com.habitflow.newapp.data.model.Streak
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    val currentUser: FirebaseUser? get() = auth.currentUser
    fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            val profile = mapOf(
                "xp" to 0,
                "createdAt" to SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
                "displayName" to (user.displayName ?: email.substringBefore("@"))
            )
            db.collection("users").document(user.uid).set(profile, SetOptions.merge()).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogleToken(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user!!
            val profile = mapOf(
                "xp" to 0,
                "createdAt" to SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
                "displayName" to (user.displayName ?: "User")
            )
            db.collection("users").document(user.uid).set(profile, SetOptions.merge()).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() = auth.signOut()

    fun habitsFlow(uid: String): Flow<List<Habit>> = callbackFlow {
        val ref = db.collection("users").document(uid).collection("habits")
            .orderBy("position")
        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val habits = snapshot?.documents?.map { doc ->
                Habit(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    icon = doc.getString("icon") ?: "🎯",
                    color = doc.getString("color") ?: "#6366f1",
                    priority = doc.getString("priority") ?: "Medium",
                    schedule = doc.getString("schedule") ?: "Daily",
                    reminder = doc.getString("reminder") ?: "",
                    notes = doc.getString("notes") ?: "",
                    position = doc.getLong("position")?.toInt() ?: 0,
                    createdAt = doc.getString("createdAt") ?: ""
                )
            } ?: emptyList()
            trySend(habits)
        }
        awaitClose { listener.remove() }
    }

    suspend fun addHabit(uid: String, habit: Habit): String {
        val data = mapOf(
            "name" to habit.name,
            "icon" to habit.icon,
            "color" to habit.color,
            "priority" to habit.priority,
            "schedule" to habit.schedule,
            "reminder" to habit.reminder,
            "notes" to habit.notes,
            "position" to habit.position,
            "createdAt" to SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        )
        val ref = db.collection("users").document(uid).collection("habits").add(data).await()
        return ref.id
    }

    suspend fun updateHabit(uid: String, habitId: String, habit: Habit) {
        val data = mapOf(
            "name" to habit.name,
            "icon" to habit.icon,
            "color" to habit.color,
            "priority" to habit.priority,
            "schedule" to habit.schedule,
            "reminder" to habit.reminder,
            "notes" to habit.notes,
            "position" to habit.position
        )
        db.collection("users").document(uid).collection("habits").document(habitId)
            .update(data).await()
    }

    suspend fun deleteHabit(uid: String, habitId: String) {
        db.collection("users").document(uid).collection("habits").document(habitId)
            .delete().await()
    }
    fun completionsFlow(uid: String): Flow<Map<String, Map<String, Boolean>>> = callbackFlow {
        val ref = db.collection("users").document(uid).collection("completions")
        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyMap())
                return@addSnapshotListener
            }
            val comps = mutableMapOf<String, Map<String, Boolean>>()
            snapshot?.documents?.forEach { doc ->
                val dayMap = mutableMapOf<String, Boolean>()
                doc.data?.forEach { (key, value) ->
                    if (value is Boolean) {
                        dayMap[key] = value
                    }
                }
                comps[doc.id] = dayMap
            }
            trySend(comps)
        }
        awaitClose { listener.remove() }
    }

    suspend fun setCompletion(uid: String, date: String, habitId: String, completed: Boolean) {
        val ref = db.collection("users").document(uid).collection("completions").document(date)
        if (completed) {
            ref.set(mapOf(habitId to true), SetOptions.merge()).await()
        } else {
            ref.update(mapOf(habitId to com.google.firebase.firestore.FieldValue.delete())).await()
        }
    }
    fun xpFlow(uid: String): Flow<Int> = callbackFlow {
        val ref = db.collection("users").document(uid)
        val listener = ref.addSnapshotListener { snapshot, _ ->
            val xp = snapshot?.getLong("xp")?.toInt() ?: 0
            trySend(xp)
        }
        awaitClose { listener.remove() }
    }

    suspend fun updateXp(uid: String, xp: Int) {
        db.collection("users").document(uid).update("xp", xp).await()
    }

    fun streaksFlow(uid: String): Flow<Map<String, Streak>> = callbackFlow {
        val ref = db.collection("users").document(uid).collection("meta").document("streaks")
        val listener = ref.addSnapshotListener { snapshot, _ ->
            val map = mutableMapOf<String, Streak>()
            snapshot?.data?.forEach { (habitId, value) ->
                if (value is Map<*, *>) {
                    map[habitId] = Streak(
                        current = (value["current"] as? Long)?.toInt() ?: 0,
                        longest = (value["longest"] as? Long)?.toInt() ?: 0
                    )
                }
            }
            trySend(map)
        }
        awaitClose { listener.remove() }
    }

    suspend fun updateStreaks(uid: String, streaks: Map<String, Streak>) {
        val data = streaks.mapValues { (_, s) ->
            mapOf("current" to s.current, "longest" to s.longest)
        }
        db.collection("users").document(uid).collection("meta").document("streaks")
            .set(data).await()
    }
}
