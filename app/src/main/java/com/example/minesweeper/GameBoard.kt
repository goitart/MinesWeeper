package com.example.minesweeper

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.zoom.Alignment
import com.otaliastudios.zoom.ZoomLayout
import kotlin.random.Random

class GameBoard : AppCompatActivity() {
    private var fieldSize = 9
    private var bombs = 10
    private var numbOfOpened = 0
    private var isFirstClick = true

    private lateinit var chronometer: Chronometer
    var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("states", "ActivityTwo: onCreate()")
        setContentView(R.layout.activity_game_board)

        val zoom = findViewById<View>(R.id.layoutForField) as ZoomLayout
        zoom.setAlignment(Alignment.NONE_HORIZONTAL)
        zoom.setMaxZoom(10.0F)

        fieldSize = intent.getIntExtra("fieldSize", 9)
        bombs = intent.getIntExtra("numbOfBombs", 10)

        fieldArray = Array(fieldSize) {
            arrayOfNulls(fieldSize)
        }
        arrayOfCells = Array(fieldSize) {
            arrayOfNulls(fieldSize)
        }

        Log.d("states", "$fieldSize")
        attributes()
        fieldDesign()
    }

    private fun chronometerStart() {
        chronometer = findViewById<View>(R.id.chronometer) as Chronometer
        SystemClock.elapsedRealtime()
        chronometer.setOnChronometerTickListener {
            (SystemClock.elapsedRealtime() - chronometer.base)
        }
        chronometer.base = SystemClock.elapsedRealtime()
    }

    private fun newGame() {
        val restartBtn = findViewById<View>(R.id.restart)
        restartBtn.setOnClickListener {
            recreate()
        }
    }

    fun toNewGameSettings(view: View) {
        val intent = Intent(this, NewGameSettings::class.java)
        finishAndRemoveTask()
        startActivity(intent)
    }

    private var fieldArray = Array(fieldSize) {
        arrayOfNulls<ImageView>(fieldSize)
    }
    private var arrayOfCells = Array(fieldSize) {
        arrayOfNulls<FieldCell>(fieldSize)
    }

    private val cell = arrayOfNulls<Drawable>(13)
    private fun attributes() {
        cell[0] = null
        cell[1] = this.resources.getDrawable(R.drawable.new_bomb)
        cell[2] = this.resources.getDrawable(R.drawable.small_flag)
        cell[3] = this.resources.getDrawable(R.drawable.red_cell)
        cell[4] = this.resources.getDrawable(R.drawable.ic_vector_cell)
        cell[5] = this.resources.getDrawable(R.drawable.one)
        cell[6] = this.resources.getDrawable(R.drawable.two)
        cell[7] = this.resources.getDrawable(R.drawable.three)
        cell[8] = this.resources.getDrawable(R.drawable.four)
        cell[9] = this.resources.getDrawable(R.drawable.five)
        cell[10] = this.resources.getDrawable(R.drawable.six)
        cell[11] = this.resources.getDrawable(R.drawable.seven)
        cell[12] = this.resources.getDrawable(R.drawable.eight)
    }

    private fun numbOfBombs(i: Int, k: Int) {
        var count = 0
        val isIZero = (i != 0)
        val isKZero = (k != 0)
        val isIEight = (i != (fieldSize - 1))
        val isKEight = (k != (fieldSize - 1))

        if (isIZero) {
            if (arrayOfCells[i - 1][k]!!.isBomb == 1) count++
            if (isKZero) {
                if (arrayOfCells[i - 1][k - 1]!!.isBomb == 1) count++
            }
        }
        if (isIEight) {
            if (arrayOfCells[i + 1][k]!!.isBomb == 1) count++
            if (isKEight) {
                if (arrayOfCells[i + 1][k + 1]!!.isBomb == 1) count++
            }
        }
        if (isKZero) {
            if (arrayOfCells[i][k - 1]!!.isBomb == 1) count++
            if (isIEight) {
                if (arrayOfCells[i + 1][k - 1]!!.isBomb == 1) count++
            }
        }
        if (isKEight) {
            if (arrayOfCells[i][k + 1]!!.isBomb == 1) count++
            if (isIZero) {
                if (arrayOfCells[i - 1][k + 1]!!.isBomb == 1) count++
            }
        }
        arrayOfCells[i][k]!!.value = count
    }

    private fun openFieldByClick(i: Int, k: Int) {
        val isINotZero = (i != 0)
        val isKNotZero = (k != 0)
        val isINotEight = (i != (fieldSize - 1))
        val isKNotEight = (k != (fieldSize - 1))
        if (isINotZero && !arrayOfCells[i - 1][k]!!.isOpened) {
            arrayOfCells[i - 1][k]!!.isOpened = true
            click(i - 1, k)
            if (isKNotZero && !arrayOfCells[i - 1][k - 1]!!.isOpened) {
                arrayOfCells[i - 1][k - 1]!!.isOpened = true
                click(i - 1, k - 1)
            }
        }
        if (isINotEight && !arrayOfCells[i + 1][k]!!.isOpened) {
            arrayOfCells[i + 1][k]!!.isOpened = true
            click(i + 1, k)
            if (isKNotEight && !arrayOfCells[i + 1][k + 1]!!.isOpened) {
                arrayOfCells[i + 1][k + 1]!!.isOpened = true
                click(i + 1, k + 1)
            }
        }
        if (isKNotZero && !arrayOfCells[i][k - 1]!!.isOpened) {
            arrayOfCells[i][k - 1]!!.isOpened = true
            click(i, k - 1)
            if (isINotEight && !arrayOfCells[i + 1][k - 1]!!.isOpened) {
                arrayOfCells[i + 1][k - 1]!!.isOpened = true
                click(i + 1, k - 1)
            }
        }
        if (isKNotEight && !arrayOfCells[i][k + 1]!!.isOpened) {
            arrayOfCells[i][k + 1]!!.isOpened = true
            click(i, k + 1)
            if (isINotZero && !arrayOfCells[i - 1][k + 1]!!.isOpened) {
                arrayOfCells[i - 1][k + 1]!!.isOpened = true
                click(i - 1, k + 1)
            }
        }
    }

    private fun click(i: Int, k: Int) {
        if (arrayOfCells[i][k]!!.isBomb == 0 && !arrayOfCells[i][k]!!.isChecked) {
            arrayOfCells[i][k]!!.isChecked = true
            numbOfOpened++
        }
        if (numbOfOpened == ((fieldSize) * (fieldSize) - bombs)) {
            for (a in 0 until fieldSize) {
                for (b in 0 until fieldSize) {
                    arrayOfCells[a][b]!!.isClickable = false
                }
            }
            Toast.makeText(this, "U win", Toast.LENGTH_SHORT).show()
        }

        if (arrayOfCells[i][k]!!.isBomb == 0 && arrayOfCells[i][k]!!.isClickable) {
            val bombs = arrayOfCells[i][k]!!.value
            if (bombs == 0) fieldArray[i][k]!!.background = cell[0]
            if (bombs == 1) fieldArray[i][k]!!.background = cell[5]
            if (bombs == 2) fieldArray[i][k]!!.background = cell[6]
            if (bombs == 3) fieldArray[i][k]!!.background = cell[7]
            if (bombs == 4) fieldArray[i][k]!!.background = cell[8]
            if (bombs == 5) fieldArray[i][k]!!.background = cell[9]
            if (bombs == 6) fieldArray[i][k]!!.background = cell[10]
            if (bombs == 7) fieldArray[i][k]!!.background = cell[11]
            if (bombs == 8) fieldArray[i][k]!!.background = cell[12]
        }
        if (arrayOfCells[i][k]!!.value == 0 && arrayOfCells[i][k]!!.isClickable) {
            openFieldByClick(i, k)
        }
    }

    private fun clickActivity(i: Int, k: Int, switcher: ToggleButton) {
        fieldArray[i][k]!!.setOnClickListener {
            if (switcher.isChecked) {
                if (!arrayOfCells[i][k]!!.isFlag && arrayOfCells[i][k]!!.isClickable && !arrayOfCells[i][k]!!.isOpened) {
                    val finalDrawable = LayerDrawable(arrayOf(cell[4], cell[2]))
                    fieldArray[i][k]!!.background = finalDrawable
                    arrayOfCells[i][k]!!.isFlag = true
                } else if (arrayOfCells[i][k]!!.isFlag && arrayOfCells[i][k]!!.isClickable) {
                    fieldArray[i][k]!!.background = cell[4]
                    arrayOfCells[i][k]!!.isFlag = false
                }
            } else if (!switcher.isChecked) {

                if (isFirstClick) {
                    chronometerStart()
                    isStarted = true
                    chronometer.start()
                    arrayOfCells[i][k]!!.isClickable = false
                    if (arrayOfCells[i][k]!!.isBomb == 1) {
                        arrayOfCells[i][k]!!.isBomb = 0
                        var isRelocated = false
                        while (!isRelocated) {
                            val a = Random.nextInt(0, fieldSize)
                            val b = Random.nextInt(0, fieldSize)
                            if (arrayOfCells[a][b]!!.isBomb == 0 && (a != i || b != k)) {
                                arrayOfCells[a][b]!!.isBomb = 1
                                isRelocated = true
                            }
                        }
                    }
                    for (a in 0 until fieldSize) {
                        for (b in 0 until fieldSize) {
                            numbOfBombs(a, b)
                        }
                    }
                    val bombs = arrayOfCells[i][k]!!.value
                    if (bombs == 0) fieldArray[i][k]!!.background = cell[0]
                    if (bombs == 1) fieldArray[i][k]!!.background = cell[5]
                    if (bombs == 2) fieldArray[i][k]!!.background = cell[6]
                    if (bombs == 3) fieldArray[i][k]!!.background = cell[7]
                    if (bombs == 4) fieldArray[i][k]!!.background = cell[8]
                    if (bombs == 5) fieldArray[i][k]!!.background = cell[9]
                    if (bombs == 6) fieldArray[i][k]!!.background = cell[10]
                    if (bombs == 7) fieldArray[i][k]!!.background = cell[11]
                    if (bombs == 8) fieldArray[i][k]!!.background = cell[12]
                    isFirstClick = false
                }

                if (arrayOfCells[i][k]!!.isBomb == 0 && arrayOfCells[i][k]!!.isClickable) {
                    val bombs = arrayOfCells[i][k]!!.value
                    if (bombs != 0 && !arrayOfCells[i][k]!!.isChecked) {
                        arrayOfCells[i][k]!!.isChecked = true
                        numbOfOpened++
                    }
                    if (numbOfOpened == ((fieldSize) * (fieldSize) - bombs)) {
                        for (a in 0 until fieldSize) {
                            for (b in 0 until fieldSize) {
                                arrayOfCells[a][b]!!.isClickable = false
                            }
                        }
                        Toast.makeText(this, "U win", Toast.LENGTH_SHORT).show()
                    }
                    if (bombs == 0) fieldArray[i][k]!!.background = cell[0]
                    if (bombs == 1) fieldArray[i][k]!!.background = cell[5]
                    if (bombs == 2) fieldArray[i][k]!!.background = cell[6]
                    if (bombs == 3) fieldArray[i][k]!!.background = cell[7]
                    if (bombs == 4) fieldArray[i][k]!!.background = cell[8]
                    if (bombs == 5) fieldArray[i][k]!!.background = cell[9]
                    if (bombs == 6) fieldArray[i][k]!!.background = cell[10]
                    if (bombs == 7) fieldArray[i][k]!!.background = cell[11]
                    if (bombs == 8) fieldArray[i][k]!!.background = cell[12]
                }
                if (arrayOfCells[i][k]!!.value == 0) {
                    openFieldByClick(i, k)
                }
                if (arrayOfCells[i][k]!!.isBomb == 1 && arrayOfCells[i][k]!!.isClickable) {
                    chronometer.stop()
                    for (a in 0 until fieldSize) {
                        for (b in 0 until fieldSize) {
                            arrayOfCells[a][b]!!.isClickable = false
                            if (arrayOfCells[a][b]!!.isBomb == 1) {
                                val redBomb = LayerDrawable(arrayOf(cell[3], cell[1]))
                                fieldArray[a][b]!!.background = redBomb
                            }
                        }
                    }
                }
            }
        }
    }

    private fun forFun(
        field: LinearLayout,
        switcher: ToggleButton,
        size: LinearLayout.LayoutParams,
        row: LinearLayout.LayoutParams
    ) {
        for (i in 0 until fieldSize) {
            val linRow = LinearLayout(this)
            for (k in 0 until fieldSize) {
                fieldArray[i][k] = ImageView(this)
                fieldArray[i][k]!!.background = cell[4]
                arrayOfCells[i][k]!!.isOpened = false
                numbOfBombs(i, k)
                if (arrayOfCells[i][k]!!.isEmpty == 1) clickActivity(i, k, switcher)
                linRow.addView(fieldArray[i][k], size)
            }
            field.addView(linRow, row)
        }
    }

    private fun fieldDesign() {
        newGame()
        val cellSize = 8000 / fieldSize
        val row: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(fieldSize * cellSize, cellSize)
        val size = LinearLayout.LayoutParams(cellSize, cellSize)
        val switcher = findViewById<View>(R.id.switcher) as ToggleButton
        val field = findViewById<View>(R.id.field) as LinearLayout
        for (i in 0 until fieldSize) {
            for (k in 0 until fieldSize) {
                arrayOfCells[i][k] = FieldCell()
            }
        }
        for (j in 0..bombs) {
            val i = Random.nextInt(0, fieldSize)
            val k = Random.nextInt(0, fieldSize)
            if (arrayOfCells[i][k]!!.isBomb == 0) arrayOfCells[i][k]!!.isBomb = 1
        }
        forFun(field, switcher, size, row)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("states", "ActivityTwo: onDestroy()")
        Log.d("states", "${searchScreen()}")
    }

    var savedTime: Long = 0
    override fun onPause() {
        super.onPause()
        if (isStarted) chronometer.stop()
        savedTime = SystemClock.elapsedRealtime()
    }

    override fun onRestart() {
        super.onRestart()
        chronometer.base = chronometer.base + (SystemClock.elapsedRealtime() - savedTime)
        chronometer.start()

    }

    private fun searchScreen(): Int {
        return (this.resources.displayMetrics.widthPixels)
    }
}