package com.example.minesweeper

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.Toast

class CustomBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_board)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.backgroundColor)
        findMaxBombs()
    }

    fun toNewGameSettings(view: View) {
        val intent = Intent(this, NewGameSettings::class.java)
        finishAndRemoveTask()
        startActivity(intent)
    }

    fun toGameBoard(view: View) {
        val intent = Intent(this, GameBoard :: class.java)
        val arr = findMaxBombs()
        intent.putExtra("gameMode", "Custom")
        intent.putExtra("fieldSizeI", arr[0])
        intent.putExtra("fieldSizeK", arr[1])
        intent.putExtra("numbOfBombs", arr[2])
        finishAndRemoveTask()
        startActivity(intent)
    }

    fun findMaxBombs() :Array<Int>{
        val pickerI = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.row)
        val pickerK = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.column)
        val pickerBombs = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.numbOfBombs)
        var boardSize: Int
        var iSize = 7
        var kSize = 7
        pickerI.setOnValueChangedListener {_, _, newVal ->
            iSize = newVal
            Toast.makeText(this, "$iSize", Toast.LENGTH_SHORT).show()
            boardSize = iSize * kSize
            pickerBombs.maxValue = boardSize / 3
        }
        pickerK.setOnValueChangedListener {_, _, newVal ->
            kSize = newVal
            boardSize = iSize * kSize
            pickerBombs.maxValue = boardSize / 3
        }
        return arrayOf(pickerI.value, pickerK.value, pickerBombs.value)
    }
}