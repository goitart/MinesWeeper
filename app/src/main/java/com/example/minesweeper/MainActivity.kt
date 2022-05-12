package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DynamicColors.applyIfAvailable(this, R.style.Theme_MinesWeeper)
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