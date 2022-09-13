package com.example.minesweeper

import android.widget.ImageView

class FieldOfCells {
    var fieldSizeK = 9
    var fieldSizeI = 9
    var fieldArray = Array(fieldSizeI) { //для визуального поля
        arrayOfNulls<ImageView>(fieldSizeK)
    }
    var arrayOfCells = Array(fieldSizeI) { //для определения каждой клетки
        arrayOfNulls<FieldCell>(fieldSizeK)
    }
}