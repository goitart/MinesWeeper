package com.example.minesweeper

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker

class CustomBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_board)
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
        intent.putExtra("fieldSize", 10)
        intent.putExtra("fieldSizeI", arr[0])
        intent.putExtra("fieldSizeK", arr[1])
        intent.putExtra("numbOfBombs", arr[2])
        startActivity(intent)
    }

    fun findMaxBombs() :Array<Int>{
        val pickerI = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.row)
        val pickerK = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.column)
        val pickerBombs = findViewById<com.shawnlin.numberpicker.NumberPicker>(R.id.numbOfBombs)
        var boardSize = 0
        var ISize = 5
        var KSize = 5
        pickerI.setOnValueChangedListener {_, _, newVal ->
            ISize = newVal
            boardSize = ISize * KSize
            pickerBombs.maxValue = boardSize / 2
        }
        pickerK.setOnValueChangedListener {_, _, newVal ->
            KSize = newVal
            boardSize = ISize * KSize
            pickerBombs.maxValue = boardSize / 2
        }
        return arrayOf(pickerI.value, pickerK.value, pickerBombs.value)
    }
}