# Command Center (Android) 📱

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badgelogo=androidlogoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badgelogo=kotlinlogoColor=white) ![Status](https://img.shields.io/badge/Status-Stable-success?style=for-the-badge)


> **Status:** V1.0 (Stable)  
> **Built On:** Headless Linux Terminal (Cloud Environment)

A "Hacker-Style" utility app that lives in your Quick Settings panel. It features a glassmorphism UI, floating windows, and system diagnostic tools. Built entirely via command line without Android Studio.

---

## 🚀 Features

### 1. Dual Launch Modes
- **Quick Tile Mode:** Opens as a floating, semi-transparent "Glass" popup (draggable).
- **App Icon Mode:** Opens as a full-screen app with settings and uninstall options.

### 2. Tools
- **📝 Flash Notes:** A quick scratchpad that saves to internal storage.
- **📟 System Monitor:** Matrix-style real-time RAM, Battery, and Uptime stats.
- **🌐 Net Analyzer:** View Local IP, Signal Strength (dBm), and Link Speed.

### 3. "Pro" Features
- **🎨 Icon Switcher:** Toggle between Terminal, Coffee, and Lightning icons.
- **🖱️ Draggable Windows:** Custom touch logic to move and resize tools.
- **🧪 Liquid Glass UI:** Custom blur-style background for popups.

---

## 🛠️ How to Build (CLI / Cloud Terminal)

If you are building this in a **Headless Environment** (VS Code Web, Gitpod, Codespaces, VPS), follow these steps strictly to avoid common errors.

### 1. Prerequisites
You need the following installed manually:
* **Java 17** (Mandatory. Java 11 will cause build failures).
* **Android SDK Command Line Tools** (cmdline-tools).
* **Gradle 8.0+**.

### 2. Configuration (Crucial!)
Headless environments generally do not auto-detect SDK paths. You must create a `local.properties` file in the root directory:

```bash
echo "sdk.dir=/home/coder/your_sdk_path" > local.properties
```

### 3. Build Command
```bash
# If using a local gradle distribution:
./gradle-8.2/bin/gradle assembleDebug

# If using the wrapper (standard):
./gradlew assembleDebug
```

**Output Location:** `app/build/outputs/apk/debug/Command_Center-debug.apk`

---

## ⚠️ Troubleshooting (Common CLI Errors)

### ❌ Error: "Android Gradle plugin requires Java 17"
**Cause:** Your terminal defaults to Java 11, but Android 13+ builds require Java 17.  
**Fix:** Force the project to use your specific Java folder.
```bash
# Run this in the project root
echo "org.gradle.java.home=/path/to/your/jdk-17" >> gradle.properties
```

### ❌ Error: "SDK location not found"
**Cause:** The environment variable `ANDROID_HOME` is missing or not picked up by Gradle.  
**Fix:** Manually set the path in `local.properties` (See step 2 above).

### ❌ Error: "Too many active changes" (Git)
**Cause:** You accidentally initialized Git in a folder containing the huge SDK or JDK directories.  
**Fix:** Update your `.gitignore` to exclude tools, then clear the cache:
```bash
git rm -r --cached .
git add .
```

---

## 📸 Installation
1. Download the APK file from the output folder.
2. Install on your Android device.
3. **Important:** Add the "Command Center" tile to your Quick Settings panel to use the popup features.
