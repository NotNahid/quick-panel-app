package com.example.quicktiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.service.quicksettings.TileService
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class DashboardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. DETECT SOURCE
        val source = intent.getStringExtra("LAUNCH_SOURCE") ?: "APP"
        val isTileMode = (source == "TILE")

        // 2. WINDOW SETUP
        if (isTileMode) {
            // Popup Mode (Fixed Size)
            val winParams = window.attributes
            winParams.width = 900
            winParams.height = 800
            window.attributes = winParams
        } else {
            // App Mode (Full Screen)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // 3. MAIN CONTAINER
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(30, 30, 30, 30)
        
        // --- LIQUID GLASS UI ---
        if (isTileMode) {
            container.setBackgroundResource(R.drawable.bg_glass)
        } else {
            container.setBackgroundColor(Color.parseColor("#121212")) // Solid Dark for App
        }
        
        val title = TextView(this)
        title.text = if (isTileMode) "QUICK MENU" else "COMMAND CENTER"
        title.textSize = 14f
        title.setTextColor(Color.LTGRAY)
        title.gravity = Gravity.CENTER
        title.setPadding(0, 10, 0, 30)

        // --- GRID (Removed Timer) ---
        val grid = GridLayout(this)
        grid.columnCount = 2
        grid.alignmentMode = GridLayout.ALIGN_BOUNDS

        val btn1 = createBtn("📝 NOTES", "#1E88E5")
        btn1.setOnClickListener { startActivity(Intent(this, FlashNoteActivity::class.java)) }
        val btn2 = createBtn("📟 MONITOR", "#43A047")
        btn2.setOnClickListener { startActivity(Intent(this, SysMonitorActivity::class.java)) }
        val btn3 = createBtn("🌐 NET STAT", "#5E35B1")
        btn3.setOnClickListener { startActivity(Intent(this, NetStatActivity::class.java)) }
        
        grid.addView(btn1)
        grid.addView(btn2)
        grid.addView(btn3)

        // --- SETTINGS ROW (Only for App Mode) ---
        val settingsLayout = LinearLayout(this)
        settingsLayout.orientation = LinearLayout.HORIZONTAL
        settingsLayout.setPadding(0, 50, 0, 0)
        settingsLayout.weightSum = 2f

        if (!isTileMode) {
            val iconBtn = Button(this)
            iconBtn.text = "🎨 CHANGE ICON"
            iconBtn.textSize = 11f
            iconBtn.setTextColor(Color.BLACK)
            iconBtn.setBackgroundColor(Color.LTGRAY)
            val iconParams = LinearLayout.LayoutParams(0, 140, 1f)
            iconParams.setMargins(10, 0, 10, 0)
            iconBtn.layoutParams = iconParams
            iconBtn.setOnClickListener { cycleIcon() }

            val uninstallBtn = Button(this)
            uninstallBtn.text = "🗑️ UNINSTALL"
            uninstallBtn.textSize = 11f
            uninstallBtn.setTextColor(Color.WHITE)
            uninstallBtn.setBackgroundColor(Color.parseColor("#B00020"))
            val uninstParams = LinearLayout.LayoutParams(0, 140, 1f)
            uninstParams.setMargins(10, 0, 10, 0)
            uninstallBtn.layoutParams = uninstParams
            
            uninstallBtn.setOnClickListener {
                // FIXED UNINSTALL LOGIC
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:$packageName")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Crucial Fix
                startActivity(intent)
            }

            settingsLayout.addView(iconBtn)
            settingsLayout.addView(uninstallBtn)
        }

        container.addView(title)
        container.addView(grid)
        if (!isTileMode) container.addView(settingsLayout)

        // 4. APPLY DRAG (Only if it's a popup)
        if (isTileMode) {
            FloatingWindowHelper.makeDraggable(this, container, "Menu")
        } else {
            setContentView(container)
        }
    }

    private fun cycleIcon() {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        var current = prefs.getInt("iconIndex", 1)
        current = (current + 1) % 3 
        prefs.edit().putInt("iconIndex", current).apply()
        
        val names = arrayOf("Coffee Cup", "Terminal", "Lightning")
        Toast.makeText(this, "Icon set to: " + names[current], Toast.LENGTH_SHORT).show()
        
        TileService.requestListeningState(this, android.content.ComponentName(this, NoteTileService::class.java))
    }

    private fun createBtn(text: String, colorHex: String): Button {
        val btn = Button(this)
        btn.text = text
        btn.setTextColor(Color.WHITE)
        val bg = GradientDrawable()
        bg.setColor(Color.parseColor(colorHex))
        bg.cornerRadius = 25f
        btn.background = bg
        val params = GridLayout.LayoutParams()
        params.width = 350
        params.height = 180
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        params.setMargins(15, 15, 15, 15)
        btn.layoutParams = params
        return btn
    }
}
