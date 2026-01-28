package com.example.quicktiles
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class FocusTimerActivity : Activity() {
    private var timer: CountDownTimer? = null
    private lateinit var timerText: TextView
    private var isRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val winParams = window.attributes
        winParams.width = 800
        winParams.height = 600
        window.attributes = winParams

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.WHITE)
        layout.setPadding(30, 30, 30, 30)

        timerText = TextView(this)
        timerText.text = "25:00"
        timerText.textSize = 60f
        timerText.setTextColor(Color.BLACK)
        timerText.gravity = Gravity.CENTER

        val btn = Button(this)
        btn.text = "START"
        btn.setBackgroundColor(Color.parseColor("#4CAF50"))
        btn.setTextColor(Color.WHITE)
        btn.setOnClickListener {
            if (!isRunning) {
                startTimer(); btn.text = "STOP"; btn.setBackgroundColor(Color.RED)
            } else {
                stopTimer(); btn.text = "START"; btn.setBackgroundColor(Color.parseColor("#4CAF50"))
            }
        }
        layout.addView(timerText); layout.addView(btn)
        
        // ENABLE DRAG
        FloatingWindowHelper.makeDraggable(this, layout, "Focus Timer")
    }
    private fun startTimer() {
        isRunning = true
        timer = object : CountDownTimer(25 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min = (millisUntilFinished / 1000) / 60
                val sec = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", min, sec)
            }
            override fun onFinish() { timerText.text = "DONE!"; isRunning = false }
        }.start()
    }
    private fun stopTimer() { timer?.cancel(); timerText.text = "25:00"; isRunning = false }
}
