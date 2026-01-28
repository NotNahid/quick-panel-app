package com.example.quicktiles
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

class NetStatActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val winParams = window.attributes
        winParams.width = 900
        winParams.height = 700
        window.attributes = winParams

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 40, 40, 40)
        layout.setBackgroundColor(Color.parseColor("#F0F2F5"))

        val infoText = TextView(this)
        infoText.textSize = 16f
        infoText.setTextColor(Color.BLACK)
        infoText.text = getNetworkInfo()
        layout.addView(infoText)
        
        // ENABLE DRAG
        FloatingWindowHelper.makeDraggable(this, layout, "Net Analyzer")
    }
    private fun getNetworkInfo(): String {
        val sb = StringBuilder()
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        sb.append("IP ADDR:   ").append(getIpAddress()).append("\n")
        sb.append("SPEED:     ").append(wifiInfo.linkSpeed).append(" Mbps\n")
        sb.append("SIGNAL:    ").append(wifiInfo.rssi).append(" dBm\n")
        sb.append("SSID:      ").append(wifiInfo.ssid).append("\n")
        return sb.toString()
    }
    private fun getIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (intf in Collections.list(interfaces)) {
                for (addr in Collections.list(intf.inetAddresses)) {
                    if (!addr.isLoopbackAddress && addr is Inet4Address) return addr.hostAddress ?: "Unknown"
                }
            }
        } catch (e: Exception) { }
        return "Not Connected"
    }
}
