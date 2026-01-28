package com.example.quicktiles
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import java.io.File

class FlashNoteActivity : Activity() {
    private lateinit var historyContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initial Window Size
        val winParams = window.attributes
        winParams.width = 900
        winParams.height = 1000
        window.attributes = winParams

        val mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.setPadding(30, 20, 30, 20)
        mainLayout.setBackgroundColor(Color.WHITE)

        val inputBox = EditText(this)
        inputBox.hint = "Type here..."
        inputBox.setPadding(30, 30, 30, 30)
        inputBox.minHeight = 150
        inputBox.setTextColor(Color.BLACK)
        val inputBg = GradientDrawable()
        inputBg.setColor(Color.parseColor("#F5F5F5"))
        inputBg.cornerRadius = 25f
        inputBox.background = inputBg

        val saveButton = Button(this)
        saveButton.text = "Save"
        saveButton.setTextColor(Color.WHITE)
        val btnBg = GradientDrawable()
        btnBg.setColor(Color.parseColor("#007AFF"))
        btnBg.cornerRadius = 25f
        saveButton.background = btnBg
        val btnParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        btnParams.setMargins(0, 20, 0, 20)
        saveButton.layoutParams = btnParams

        val scrollView = ScrollView(this)
        // Make scroll view fill remaining space
        val scrollParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        scrollParams.weight = 1f 
        scrollView.layoutParams = scrollParams
        
        historyContainer = LinearLayout(this)
        historyContainer.orientation = LinearLayout.VERTICAL
        scrollView.addView(historyContainer)

        saveButton.setOnClickListener {
            val text = inputBox.text.toString().trim()
            if (text.isNotEmpty()) {
                saveNote(text); inputBox.setText(""); refreshHistory()
            }
        }
        mainLayout.addView(inputBox); mainLayout.addView(saveButton); mainLayout.addView(scrollView)
        
        // ENABLE DRAG & RESIZE
        FloatingWindowHelper.makeDraggable(this, mainLayout, "Flash Notes")
        
        refreshHistory()
    }
    
    // --- File Logic Same as Before ---
    private fun saveNote(content: String) {
        try {
            val file = File(getExternalFilesDir(null), "QuickNotes_DB.txt")
            val timestamp = android.text.format.DateFormat.format("MMM dd HH:mm", java.util.Date())
            file.appendText("$timestamp###$content\n")
        } catch (e: Exception) {}
    }
    private fun refreshHistory() {
        historyContainer.removeAllViews()
        val file = File(getExternalFilesDir(null), "QuickNotes_DB.txt")
        if (!file.exists()) return
        val lines = file.readLines().reversed()
        for (line in lines) {
            if (line.contains("###")) {
                val parts = line.split("###")
                if (parts.size >= 2) addCard(parts[0], parts[1], line)
            }
        }
    }
    private fun addCard(date: String, text: String, originalLine: String) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.setPadding(30, 20, 30, 20)
        val cardBg = GradientDrawable()
        cardBg.setColor(Color.WHITE)
        cardBg.setStroke(3, Color.parseColor("#EEEEEE"))
        cardBg.cornerRadius = 20f
        card.background = cardBg
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0, 20)
        card.layoutParams = params
        val noteView = TextView(this)
        noteView.text = text
        noteView.textSize = 16f
        noteView.setTextColor(Color.BLACK)
        card.addView(noteView)
        card.setOnLongClickListener { deleteNote(originalLine); true }
        historyContainer.addView(card)
    }
    private fun deleteNote(lineToRemove: String) {
        val file = File(getExternalFilesDir(null), "QuickNotes_DB.txt")
        val lines = file.readLines()
        file.writeText("")
        for (line in lines) { if (line != lineToRemove) file.appendText(line + "\n") }
        refreshHistory()
    }
}
