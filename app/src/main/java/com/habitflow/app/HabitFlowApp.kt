package com.habitflow.newapp

import android.app.Application
import com.google.firebase.FirebaseApp

class HabitFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
