package com.example.quicktiles
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.LinearLayout
import android.widget.TextView

class SysMonitorActivity : Activity() {
    private lateinit var infoText: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() { refreshStats(); handler.postDelayed(this, 1000) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val winParams = window.attributes
        winParams.width = 800
        winParams.height = 700
        window.attributes = winParams

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        // Glass Background
        layout.setBackgroundResource(R.drawable.bg_glass)
        layout.setPadding(40, 20, 40, 20)
        
        infoText = TextView(this)
        infoText.setTextColor(Color.parseColor("#00FF00")) // Bright Green
        infoText.typeface = Typeface.MONOSPACE
        infoText.textSize = 12f
        layout.addView(infoText)
        
        FloatingWindowHelper.makeDraggable(this, layout, "System Monitor")
    }
    override fun onResume() { super.onResume(); handler.post(updateRunnable) }
    override fun onPause() { super.onPause(); handler.removeCallbacks(updateRunnable) }
    private fun refreshStats() {
        val sb = StringBuilder()
        sb.append("MODEL:   ${Build.MODEL}\nANDROID: ${Build.VERSION.RELEASE}\n\n")
        val actManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val usedMem = (memInfo.totalMem - memInfo.availMem) / (1024 * 1024)
        val percent = (usedMem * 100) / (memInfo.totalMem / (1024 * 1024))
        sb.append("[RAM]\nUSED:    $usedMem MB ($percent%)\n\n")
        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 0
        val temp = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        sb.append("[BATTERY]\nLEVEL:   ${level}%\nTEMP:    ${temp / 10.0}C\n")
        infoText.text = sb.toString()
    }
}
