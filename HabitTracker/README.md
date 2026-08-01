# Habits — Android app

A Notion-style habit tracker: Today checklist, month-calendar history, and a
home-screen widget that stays in sync. Built with Kotlin, Jetpack Compose,
Room, and Glance. Satoshi is bundled as the app font.

> **Note on the APK:** the `.apk` was **not** pre-compiled — building one needs
> the Android SDK + Google's Maven servers. This is the complete, buildable
> source project. Pick either route below to get an installable APK.

---

## Route A — Build in the cloud (no local setup)

1. Create a new **empty** GitHub repo.
2. Push this project to it:
   ```bash
   cd HabitTracker
   git init && git add . && git commit -m "Habit tracker"
   git branch -M main
   git remote add origin https://github.com/<you>/<repo>.git
   git push -u origin main
   ```
3. Open the repo's **Actions** tab. The included workflow (`.github/workflows/build.yml`)
   runs automatically and builds the APK.
4. When it finishes (~3–5 min), open the run and download the
   **`habits-debug-apk`** artifact. Inside is `app-debug.apk`.
5. Copy it to your phone and install (allow "install from unknown sources").

## Route B — Android Studio

1. **File → Open** and select the `HabitTracker` folder.
2. Let Gradle sync (first sync downloads dependencies).
3. Plug in a device (USB debugging on) or start an emulator, press **Run**;
   or **Build → Build App Bundle(s) / APK(s) → Build APK(s)** for a file.

---

## Stack / versions
Gradle 8.9 · AGP 8.6.1 · Kotlin 2.0.21 · Compose BOM 2024.10.01 ·
Glance 1.1.0 · Room 2.6.1 · minSdk 26 · target/compileSdk 34 · JDK 17.

## Notes
- The home-screen widget uses the system font (Glance/RemoteViews can't load a
  bundled custom font); the app itself uses Satoshi.
- Toggling a habit anywhere — Today screen or widget — writes to the same Room
  record, so the calendar and the widget update together.
- These files were code-reviewed but not compiled here (no Android SDK in the
  authoring environment). If CI or Android Studio flags a library-version
  mismatch, bumping the flagged version is almost always the fix.
