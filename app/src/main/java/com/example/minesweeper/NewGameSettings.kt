package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager

class NewGameSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game_settings)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.backgroundColor)
    }
    fun toBeginnerMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        finishAndRemoveTask()
        intent.putExtra("gameMode", "Beginner")
        intent.putExtra("fieldSizeI", 9)
        intent.putExtra("fieldSizeK", 9)
        intent.putExtra("numbOfBombs", 10)
        startActivity(intent)
    }
    fun toIntermMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        finishAndRemoveTask()
        intent.putExtra("gameMode", "Intermediate")
        intent.putExtra("fieldSizeI", 16)
        intent.putExtra("fieldSizeK", 16)
        intent.putExtra("numbOfBombs", 40)
        startActivity(intent)
    }
    fun toExpertMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        finishAndRemoveTask()
        intent.putExtra("gameMode", "Expert")
        intent.putExtra("fieldSizeI", 24)
        intent.putExtra("fieldSizeK", 24)
        intent.putExtra("numbOfBombs", 99)
        startActivity(intent)
    }
    fun toCustomBoard(view: View) {
        finishAndRemoveTask()
        val intent = Intent(this, CustomBoard :: class.java)
        startActivity(intent)
    }
    fun toMainActivity(view: View) {
        val intent = Intent(this, MainActivity :: class.java)
        finishAndRemoveTask()
        startActivity(intent)
    }
}