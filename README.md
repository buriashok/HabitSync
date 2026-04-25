<div align="center">

# 🔄 HabitSync

**Build better habits. Track your progress. Level up your life.**

A modern Android habit tracking app built with Jetpack Compose, Firebase, and a gamified XP system to keep you motivated every day.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?logo=android)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28.svg?logo=firebase)](https://firebase.google.com)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-orange.svg)](https://developer.android.com/about/versions/oreo)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

</div>

---

## 📖 About

HabitSync is an Android application designed to help users build and maintain positive habits through gamification, cloud sync, and smart analytics. Create custom habits, track daily completions, earn XP points, climb the leaderboard, and get personalized advice from your AI Coach — all synced seamlessly across your devices via Firebase.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏠 **Dashboard** | Daily overview with progress ring, streak counter, XP/level bar, daily challenge, and motivational quote |
| ✅ **Habit Management** | Create, edit, reorder, and delete habits with custom icons, colors, priorities, and schedules |
| 📊 **Analytics** | Visual charts and insights into your completion rates, streaks, and habit trends |
| 📅 **Calendar View** | See your habit completion history on a monthly calendar |
| ⏱️ **Focus Timer** | Built-in Pomodoro-style timer to stay focused while working on habits |
| 🏆 **Leaderboard** | Global and friends leaderboards ranked by XP, streaks, and completion rates |
| 🤖 **AI Coach** | Chat with an AI-powered habit coach for personalized tips and motivation |
| 👤 **Profile** | View your stats, achievements, badges, and manage your account |
| 🎮 **Gamification** | XP points, levels, streaks, achievements, and daily challenges |
| ☁️ **Cloud Sync** | All habit data synced to Firebase Firestore across devices |
| 🔐 **Authentication** | Secure login via Google Sign-In and Firebase Auth |
| 🌙 **Dark/Light Theme** | Adaptive theming for comfortable use at any time of day |

---

## 🛠️ Tech Stack

### Core
- **Language:** [Kotlin](https://kotlinlang.org)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Architecture:** MVVM (ViewModel + StateFlow)
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)

### Backend & Data
- **Authentication:** [Firebase Authentication](https://firebase.google.com/products/auth) + [Google Sign-In](https://developers.google.com/identity/sign-in/android)
- **Database:** [Firebase Firestore](https://firebase.google.com/products/firestore)
- **Local Storage:** [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)

### Networking & Media
- **HTTP Client:** [Retrofit 2](https://square.github.io/retrofit/) + Gson
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/) for Compose

### UI & UX
- **Animations:** Jetpack Compose Animation APIs
- **Splash Screen:** AndroidX Core SplashScreen
- **Icons:** Material Icons Extended

---

## 📋 Requirements

- **Android Studio:** Hedgehog (2023.1.1) or newer
- **Android SDK:** API 35 (compile), API 26 minimum (Android 8.0+)
- **JDK:** 17
- **Google Services:** A `google-services.json` file from your Firebase project

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/buriashok/HabitSync.git
cd HabitSync
```

### 2. Set up Firebase
1. Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
2. Register an Android app with package name `com.habitflow.newapp`.
3. Download the `google-services.json` file and place it in the `app/` directory.
4. Enable **Firebase Authentication** (Google Sign-In provider).
5. Enable **Cloud Firestore** in your Firebase project.

### 3. Configure Google Sign-In
- Add your **SHA-1** fingerprint to the Firebase project settings (Project Settings → Your App → Add fingerprint).
  ```bash
  ./gradlew signingReport
  ```

### 4. Build & Run
Open the project in **Android Studio**, sync Gradle, and run the app on an emulator or physical device (API 26+).

---

## 📁 Project Structure

```
app/src/main/java/com/habitflow/app/
├── HabitFlowApp.kt              # Application class
├── MainActivity.kt              # Single Activity entry point
├── data/
│   ├── firebase/
│   │   └── FirebaseRepository.kt  # Firestore read/write operations
│   ├── local/
│   │   └── HabitData.kt           # Local data helpers & mock data
│   └── model/
│       └── Models.kt              # Data classes (Habit, Streak, UserProfile, …)
├── navigation/
│   └── NavGraph.kt              # Compose NavHost & screen routing
├── ui/
│   ├── components/
│   │   ├── BottomNavBar.kt      # Bottom navigation bar
│   │   └── SharedComponents.kt  # Reusable composables (cards, buttons, …)
│   ├── screens/
│   │   ├── ai/                  # AI Coach chat screen
│   │   ├── analytics/           # Analytics & insights screen
│   │   ├── calendar/            # Monthly habit calendar
│   │   ├── dashboard/           # Home dashboard
│   │   ├── habits/              # Habit list & management
│   │   ├── leaderboard/         # Global & friends leaderboard
│   │   ├── login/               # Google Sign-In screen
│   │   ├── profile/             # User profile & settings
│   │   └── timer/               # Focus / Pomodoro timer
│   └── theme/
│       ├── Color.kt             # App color palette
│       ├── Theme.kt             # Material 3 theme definition
│       └── Type.kt              # Typography
└── viewmodel/
    └── HabitViewModel.kt        # Central ViewModel for habit state
```

---

## 🤝 Contributing

Contributions are welcome! Please open an issue first to discuss what you'd like to change, then submit a pull request.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 🔒 Privacy

See [PRIVACY_POLICY.md](PRIVACY_POLICY.md) for details on how HabitSync handles your data.

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

<div align="center">
Made with ❤️ using Kotlin & Jetpack Compose
</div>
