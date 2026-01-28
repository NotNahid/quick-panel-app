package com.example.quicktiles

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

object FloatingWindowHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun makeDraggable(activity: Activity, contentLayout: View, title: String) {
        
        // 1. Setup Window for Transparency
        activity.window.setDimAmount(0f)
        activity.window.setBackgroundDrawableResource(android.R.color.transparent)

        // 2. Root Container
        val rootContainer = FrameLayout(activity)
        
        // 3. Title Bar (Drag Handle)
        val titleBar = TextView(activity)
        titleBar.text = " :: $title :: "
        titleBar.setTextColor(Color.LTGRAY)
        titleBar.gravity = Gravity.CENTER
        titleBar.setPadding(0, 20, 0, 20)
        titleBar.textSize = 12f
        // Glassy Title Background
        val titleBg = GradientDrawable()
        titleBg.setColor(Color.parseColor("#99000000")) // Semi-transparent black
        titleBg.cornerRadii = floatArrayOf(25f, 25f, 25f, 25f, 0f, 0f, 0f, 0f)
        titleBar.background = titleBg

        // 4. Resize Handle
        val resizeHandle = TextView(activity)
        resizeHandle.text = "◢"
        resizeHandle.textSize = 24f
        resizeHandle.setTextColor(Color.WHITE)
        val resizeParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        resizeParams.gravity = Gravity.BOTTOM or Gravity.END
        resizeParams.setMargins(0, 0, 10, 10)
        resizeHandle.layoutParams = resizeParams

        // 5. Hierarchy Surgery
        (contentLayout.parent as? ViewGroup)?.removeView(contentLayout)
        
        // Create a vertical layout to hold Title + Content
        val cardLayout = LinearLayout(activity)
        cardLayout.orientation = LinearLayout.VERTICAL
        cardLayout.addView(titleBar)
        cardLayout.addView(contentLayout)
        
        // Add to Root
        rootContainer.addView(cardLayout)
        rootContainer.addView(resizeHandle)

        activity.setContentView(rootContainer)

        // --- DRAG LOGIC ---
        var dX = 0f
        var dY = 0f
        titleBar.setOnTouchListener { _, event ->
            val params = activity.window.attributes
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = params.x - event.rawX
                    dY = params.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = (event.rawX + dX).toInt()
                    params.y = (event.rawY + dY).toInt()
                    activity.window.attributes = params
                }
            }
            true
        }

        // --- RESIZE LOGIC (Simplified) ---
        var initW = 0
        var initH = 0
        var touchX = 0f
        var touchY = 0f

        resizeHandle.setOnTouchListener { _, event ->
            val params = activity.window.attributes
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initW = params.width
                    initH = params.height
                    touchX = event.rawX
                    touchY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - touchX).toInt()
                    val deltaY = (event.rawY - touchY).toInt()
                    
                    // Enforce minimum size (300px)
                    params.width = (initW + deltaX).coerceAtLeast(300)
                    params.height = (initH + deltaY).coerceAtLeast(300)
                    
                    activity.window.attributes = params
                }
            }
            true
        }
    }
}
