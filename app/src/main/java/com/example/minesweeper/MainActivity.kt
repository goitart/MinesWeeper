package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.backgroundColor)
    }
    fun toNewGameSettings(view: View) {
        val intent = Intent(this, NewGameSettings :: class.java)
        startActivity(intent)
    }
    fun toSettings(view: View) {
        val intent = Intent(this, Settings :: class.java)
        startActivity(intent)
    }
    fun toStatistics(view: View) {
        val intent = Intent(this, Statistics :: class.java)
        startActivity(intent)
    }
}