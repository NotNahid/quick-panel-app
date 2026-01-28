package com.example.quicktiles

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class NoteTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileIcon()
    }

    private fun updateTileIcon() {
        val tile = qsTile ?: return
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val index = prefs.getInt("iconIndex", 1) // Default to Terminal (1)
        
        when (index) {
            0 -> tile.icon = Icon.createWithResource(this, R.drawable.ic_coffee)
            1 -> tile.icon = Icon.createWithResource(this, R.drawable.ic_terminal)
            2 -> tile.icon = Icon.createWithResource(this, R.drawable.ic_flash)
        }
        
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        val intent = Intent(this, DashboardActivity::class.java)
        
        // --- THE CRITICAL FIX ---
        // We tell the dashboard: "We are opening from the Tile!"
        intent.putExtra("LAUNCH_SOURCE", "TILE")
        
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(intent)
    }
}
