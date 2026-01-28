# 🔄 How to Restore This Project

If your cloud terminal resets or you move to a new PC, follow these steps to get everything working again in **under 2 minutes**.

### 1. Download Your Code
```bash
git clone https://github.com/NotNahid/quick-panel-app.git
cd quick-panel-app
```

### 2. Run the Auto-Setup Script
This script will automatically download Java 17, the Android SDK, and Gradle, and configure all the path settings for you.
```bash
# Give it permission to run
chmod +x setup_env.sh

# Run the installer
./setup_env.sh
```
*(Wait 1-2 minutes for downloads to finish)*

### 3. Build the App
```bash
./gradle-8.2/bin/gradle assembleDebug
```

### 4. Find Your APK
Your app will be waiting here:
`app/build/outputs/apk/debug/Command_Center-debug.apk`

---
**Note:** If you get a "Permission Denied" error when running the script, make sure you ran the `chmod +x` command in step 2.
