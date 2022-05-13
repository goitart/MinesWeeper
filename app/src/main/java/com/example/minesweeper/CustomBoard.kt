package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker

class CustomBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_board)

        val row = findViewById<NumberPicker>(R.id.row)
        row.minValue = 0
        row.maxValue = 100
        row.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        val column = findViewById<NumberPicker>(R.id.column)
        column.minValue = 0
        column.maxValue = 100
        column.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }
}