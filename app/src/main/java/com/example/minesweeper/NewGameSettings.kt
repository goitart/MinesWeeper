package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class NewGameSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game_settings)
    }
    fun toBeginnerMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        intent.putExtra("fieldSize", 9)
        intent.putExtra("numbOfBombs", 10)
        startActivity(intent)
    }
    fun toIntermMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        intent.putExtra("fieldSize", 16)
        intent.putExtra("numbOfBombs", 40)
        startActivity(intent)
    }
    fun toExpertMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        intent.putExtra("fieldSize", 24)
        intent.putExtra("numbOfBombs", 99)
        startActivity(intent)
    }
    fun toMasterMode(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        intent.putExtra("fieldSize", 50)
        intent.putExtra("numbOfBombs", 400)
        startActivity(intent)
    }
    fun toMainActivity(view: View) {
        val intent = Intent(this, MainActivity :: class.java)
        finishAndRemoveTask()
        startActivity(intent)
    }
}