package com.example.minesweeper

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.otaliastudios.zoom.Alignment
import com.otaliastudios.zoom.ZoomLayout
import java.security.SecureRandom

class GameBoard : AppCompatActivity() {
    private var bombs = 10
    private var numbOfOpened = 0
    private var isFirstClick = true
    private var gameMode = ""
    private lateinit var chronometer: Chronometer
    private var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)

        val zoom = findViewById<View>(R.id.layoutForField) as ZoomLayout
        zoom.setAlignment(Alignment.NONE_HORIZONTAL)
        zoom.setMaxZoom(10.0F)

        val fieldSizeI = intent.getIntExtra("fieldSizeI", 9)
        val fieldSizeK = intent.getIntExtra("fieldSizeK", 9)
        gameMode = intent.getStringExtra("gameMode").toString()

        bombs = intent.getIntExtra("numbOfBombs", 10)

        fieldArray = Array(fieldSizeI) {
            arrayOfNulls(fieldSizeK)
        }
        arrayOfCells = Array(fieldSizeI) {
            arrayOfNulls(fieldSizeK)
        }

        attributes()
        fieldDesign(fieldSizeI, fieldSizeK)
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

    private var fieldArray = Array(9) { //для визуального поля
        arrayOfNulls<ImageView>(9)
    }
    private var arrayOfCells = Array(9) { //для определения каждой клетки
        arrayOfNulls<FieldCell>(9)
    }

    private val cell = arrayOfNulls<Drawable>(13)
    private fun attributes() {
        cell[0] = null
        cell[1] = ContextCompat.getDrawable(this, R.drawable.new_bomb)
        cell[2] = ContextCompat.getDrawable(this, R.drawable.small_flag)
        cell[3] = ContextCompat.getDrawable(this, R.drawable.red_cell)
        cell[4] = ContextCompat.getDrawable(this, R.drawable.ic_vector_cell)
        cell[5] = ContextCompat.getDrawable(this, R.drawable.one)
        cell[6] = ContextCompat.getDrawable(this, R.drawable.two)
        cell[7] = ContextCompat.getDrawable(this, R.drawable.three)
        cell[8] = ContextCompat.getDrawable(this, R.drawable.four)
        cell[9] = ContextCompat.getDrawable(this, R.drawable.five)
        cell[10] = ContextCompat.getDrawable(this, R.drawable.six)
        cell[11] = ContextCompat.getDrawable(this, R.drawable.seven)
        cell[12] = ContextCompat.getDrawable(this, R.drawable.eight)
    }

    private fun relocateBomb(i: Int, k: Int, fieldSizeI : Int, fieldSizeK : Int) { // переставляет бомбу на другое место
        var isRelocated = false
        arrayOfCells[i][k]!!.isBomb = 0
        while (!isRelocated) {
            val secureRandom = SecureRandom()
            val a = secureRandom.nextInt(fieldSizeI)
            val b = secureRandom.nextInt(fieldSizeK)
            if (arrayOfCells[a][b]!!.isBomb == 0 && a != i && b != k && !arrayOfCells[a][b]!!.isRelocated) {
                arrayOfCells[a][b]!!.isBomb = 1
                isRelocated = true
            }
        }
    }

    private fun timeStrToSeconds(str: String): Int {
        val parts = str.split(":")
        var result = 0
        for (part in parts) {
            val number = part.toInt()
            result = result * 60 + number
        }
        return result
    }

    private fun isWon(fieldSizeI : Int, fieldSizeK : Int) { // проверяет победу
        if (numbOfOpened == ((fieldSizeI) * (fieldSizeK)) - bombs) {
            chronometer.stop()
            val time = chronometer.text.toString()
            val sharedPreference = getSharedPreferences("ChronometerTime", MODE_PRIVATE) // для записи в лидерборд
            val editor = sharedPreference.edit()
            if (sharedPreference.getInt("Base$gameMode", 0) == 0) {
                editor.putString(gameMode, time)
                editor.putInt("Base$gameMode", timeStrToSeconds(time))
                editor.apply()
            }
            if (sharedPreference.getInt("Base$gameMode", 0) > timeStrToSeconds(time)) {
                editor.putString(gameMode, time)
                editor.putInt("Base$gameMode", timeStrToSeconds(time))
                editor.apply()
            }
            for (a in 0 until fieldSizeI) {
                for (b in 0 until fieldSizeK) {
                    arrayOfCells[a][b]!!.isClickable = false
                }
            }
            Toast.makeText(this, "😎", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeAround(i: Int, k: Int, fieldSizeI : Int, fieldSizeK : Int) { // перемещает бомбы вокруг клетки
        val isINotZero = (i != 0)
        val isKNotZero = (k != 0)
        val isINotEight = (i != (fieldSizeI - 1))
        val isKNotEight = (k != (fieldSizeK - 1))

        if (isINotZero) {
            arrayOfCells[i - 1][k]!!.isRelocated = true // помечаем места, чтобы не переставить сюда же
            if (arrayOfCells[i - 1][k]!!.isBomb == 1) {
                relocateBomb(i - 1, k, fieldSizeI, fieldSizeK)
            }
        }
        if (isKNotZero && isINotZero) {
            arrayOfCells[i - 1][k - 1]!!.isRelocated = true
            if (arrayOfCells[i - 1][k - 1]!!.isBomb == 1) {
                relocateBomb(i - 1, k - 1, fieldSizeI, fieldSizeK)
            }
        }
        if (isINotEight) {
            arrayOfCells[i + 1][k]!!.isRelocated = true
            if (arrayOfCells[i + 1][k]!!.isBomb == 1) {
                relocateBomb(i + 1, k, fieldSizeI, fieldSizeK)
            }
        }
        if (isKNotEight && isINotEight) {
            arrayOfCells[i + 1][k + 1]!!.isRelocated = true
            if (arrayOfCells[i + 1][k + 1]!!.isBomb == 1) {
                relocateBomb(i + 1, k + 1, fieldSizeI, fieldSizeK)
            }
        }
        if (isKNotZero) {
            arrayOfCells[i][k - 1]!!.isRelocated = true
            if (arrayOfCells[i][k - 1]!!.isBomb == 1) {
                relocateBomb(i, k - 1, fieldSizeI, fieldSizeK)
            }
        }
        if (isINotEight && isKNotZero) {
            arrayOfCells[i + 1][k - 1]!!.isRelocated = true
            if (arrayOfCells[i + 1][k - 1]!!.isBomb == 1) {
                relocateBomb(i + 1, k - 1, fieldSizeI, fieldSizeK)
            }
        }
        if (isKNotEight) {
            arrayOfCells[i][k + 1]!!.isRelocated = true
            if (arrayOfCells[i][k + 1]!!.isBomb == 1) {
                relocateBomb(i, k + 1, fieldSizeI, fieldSizeK)
            }
        }
        if (isINotZero && isKNotEight) {
            arrayOfCells[i - 1][k + 1]!!.isRelocated = true
            if (arrayOfCells[i - 1][k + 1]!!.isBomb == 1) {
                relocateBomb(i - 1, k + 1, fieldSizeI, fieldSizeK)
            }
        }
    }

    private fun numbOfBombs(i: Int, k: Int, fieldSizeI : Int, fieldSizeK : Int) { // кол-во бомб на клетке
        var count = 0
        val isINotZero = (i != 0)
        val isKNotZero = (k != 0)
        val isINotEight = (i != (fieldSizeI - 1))
        val isKNotEight = (k != (fieldSizeK - 1))

        if (isINotZero) {
            if (arrayOfCells[i - 1][k]!!.isBomb == 1) count++
        }
        if (isKNotZero && isINotZero) {
            if (arrayOfCells[i - 1][k - 1]!!.isBomb == 1) count++
        }
        if (isINotEight) {
            if (arrayOfCells[i + 1][k]!!.isBomb == 1) count++
        }
        if (isKNotEight && isINotEight) {
            if (arrayOfCells[i + 1][k + 1]!!.isBomb == 1) count++
        }
        if (isKNotZero) {
            if (arrayOfCells[i][k - 1]!!.isBomb == 1) count++
        }
        if (isINotEight && isKNotZero) {
            if (arrayOfCells[i + 1][k - 1]!!.isBomb == 1) count++
        }
        if (isKNotEight) {
            if (arrayOfCells[i][k + 1]!!.isBomb == 1) count++
        }
        if (isINotZero && isKNotEight) {
            if (arrayOfCells[i - 1][k + 1]!!.isBomb == 1) count++
        }
        arrayOfCells[i][k]!!.value = count
    }

    private fun openFieldByClick(i: Int, k: Int, fieldSizeI : Int, fieldSizeK : Int) { // открытие пустых пространств на поле
        val isINotZero = (i != 0)
        val isKNotZero = (k != 0)
        val isINotEight = (i != (fieldSizeI - 1))
        val isKNotEight = (k != (fieldSizeK - 1))

        if (isINotZero && !arrayOfCells[i - 1][k]!!.isOpened) {
            arrayOfCells[i - 1][k]!!.isOpened = true
            click(i - 1, k, fieldSizeI, fieldSizeK)
        }

        if (isINotZero && isKNotZero && !arrayOfCells[i - 1][k - 1]!!.isOpened) {
            arrayOfCells[i - 1][k - 1]!!.isOpened = true
            click(i - 1, k - 1, fieldSizeI, fieldSizeK)
        }

        if (isINotEight && !arrayOfCells[i + 1][k]!!.isOpened) {
            arrayOfCells[i + 1][k]!!.isOpened = true
            click(i + 1, k, fieldSizeI, fieldSizeK)
        }

        if (isINotEight && isKNotEight && !arrayOfCells[i + 1][k + 1]!!.isOpened) {
            arrayOfCells[i + 1][k + 1]!!.isOpened = true
            click(i + 1, k + 1, fieldSizeI, fieldSizeK)
        }

        if (isKNotZero && !arrayOfCells[i][k - 1]!!.isOpened) {
            arrayOfCells[i][k - 1]!!.isOpened = true
            click(i, k - 1, fieldSizeI, fieldSizeK)
        }

        if (isKNotZero && isINotEight && !arrayOfCells[i + 1][k - 1]!!.isOpened) {
            arrayOfCells[i + 1][k - 1]!!.isOpened = true
            click(i + 1, k - 1, fieldSizeI, fieldSizeK)
        }

        if (isKNotEight && !arrayOfCells[i][k + 1]!!.isOpened) {
            arrayOfCells[i][k + 1]!!.isOpened = true
            click(i, k + 1, fieldSizeI, fieldSizeK)
        }

        if (isKNotEight && isINotZero && !arrayOfCells[i - 1][k + 1]!!.isOpened) {
            arrayOfCells[i - 1][k + 1]!!.isOpened = true
            click(i - 1, k + 1, fieldSizeI, fieldSizeK)
        }
    }

    private fun click(i: Int, k: Int, fieldSizeI : Int, fieldSizeK : Int) { // открывает клетку
        if (arrayOfCells[i][k]!!.isBomb == 0 && !arrayOfCells[i][k]!!.isChecked) {
            arrayOfCells[i][k]!!.isChecked = true
        }

        if (arrayOfCells[i][k]!!.isBomb == 0) {
            val bombs = arrayOfCells[i][k]!!.value
            if (bombs == 0) {
                fieldArray[i][k]!!.background = cell[0]
                numbOfOpened++
                arrayOfCells[i][k]!!.isOpened = true
            } else if (bombs in 1..8) {
                fieldArray[i][k]!!.background = cell[bombs + 4]
                numbOfOpened++
                arrayOfCells[i][k]!!.isOpened = true
            }
            arrayOfCells[i][k]!!.isClickable = false
            isWon(fieldSizeI, fieldSizeK)

        }
        if (arrayOfCells[i][k]!!.value == 0) { // если бомб нет открываем поле дальше
            openFieldByClick(i, k, fieldSizeI, fieldSizeK)
        }
    }

    private fun gameOver(fieldSizeI : Int, fieldSizeK : Int) { // открывает все бомбы и заканчивает игру
        chronometer.stop()
        Toast.makeText(this, "LOSER", Toast.LENGTH_SHORT).show()
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val milliseconds = 10L
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
        for (a in 0 until fieldSizeI) {
            for (b in 0 until fieldSizeK) {
                arrayOfCells[a][b]!!.isClickable = false
                if (arrayOfCells[a][b]!!.isBomb == 1) {
                    val redBomb = LayerDrawable(arrayOf(cell[3], cell[1]))
                    fieldArray[a][b]!!.background = redBomb
                }
            }
        }
    }

    private fun clickActivity(i: Int, k: Int, switcher: ToggleButton, fieldSizeI : Int, fieldSizeK : Int) {
        fieldArray[i][k]!!.setOnClickListener {
            if (switcher.isChecked) { // режим флага
                if (!arrayOfCells[i][k]!!.isFlag && arrayOfCells[i][k]!!.isClickable) {
                    val finalDrawable = LayerDrawable(arrayOf(cell[4], cell[2]))
                    fieldArray[i][k]!!.background = finalDrawable
                    arrayOfCells[i][k]!!.isFlag = true
                } else if (arrayOfCells[i][k]!!.isFlag && arrayOfCells[i][k]!!.isClickable) {
                    fieldArray[i][k]!!.background = cell[4]
                    arrayOfCells[i][k]!!.isFlag = false
                }
            } else if (!switcher.isChecked && arrayOfCells[i][k]!!.isClickable) { // режим не флага

                if (isFirstClick) { // для первого клика
                    chronometerStart()
                    isStarted = true
                    chronometer.start()
                    arrayOfCells[i][k]!!.isClickable = false
                    arrayOfCells[i][k]!!.isRelocated = true
                    if (arrayOfCells[i][k]!!.isBomb == 1) { // если тут бомба переставляем ее
                        arrayOfCells[i][k]!!.isBomb = 0
                        relocateBomb(i, k, fieldSizeI, fieldSizeK)
                    }

                    for (a in 0 until fieldSizeI) { // пересчитываем количество бомб
                        for (b in 0 until fieldSizeK) {
                            numbOfBombs(a, b, fieldSizeI, fieldSizeK)
                        }
                    }

                    if (arrayOfCells[i][k]!!.value != 0 && arrayOfCells[i][k]!!.isBomb == 0) { // убираем бомбы вокруг если они есть
                        removeAround(i, k, fieldSizeI, fieldSizeK)
                    }
                    for (a in 0 until fieldSizeI) { // пересчитываем количество бомб
                        for (b in 0 until fieldSizeK) {
                            numbOfBombs(a, b, fieldSizeI, fieldSizeK)
                        }
                    }
                    val bombs = arrayOfCells[i][k]!!.value
                    if (arrayOfCells[i][k]!!.isBomb == 0) {

                        if (bombs == 0) {
                            fieldArray[i][k]!!.background = cell[0]
                            numbOfOpened++
                            arrayOfCells[i][k]!!.isOpened = true
                        } else if (bombs in 1..8) {
                            fieldArray[i][k]!!.background = cell[bombs + 4]
                            numbOfOpened++
                            arrayOfCells[i][k]!!.isOpened = true
                        }
                        arrayOfCells[i][k]!!.isClickable = false
                        isWon(fieldSizeI, fieldSizeK)
                    }
                    isFirstClick = false

                } else if (arrayOfCells[i][k]!!.isBomb == 0) { // уже не первый клик
                    val bombs = arrayOfCells[i][k]!!.value
                    if (bombs != 0 && !arrayOfCells[i][k]!!.isChecked) {
                        arrayOfCells[i][k]!!.isChecked = true
                    }

                    if (bombs == 0) {
                        fieldArray[i][k]!!.background = cell[0]
                        numbOfOpened++
                        arrayOfCells[i][k]!!.isOpened = true
                    } else if (bombs in 1..8) {
                        fieldArray[i][k]!!.background = cell[bombs + 4]
                        numbOfOpened++
                        arrayOfCells[i][k]!!.isOpened = true
                    }
                    arrayOfCells[i][k]!!.isClickable = false
                    isWon(fieldSizeI, fieldSizeK)
                    if (arrayOfCells[i][k]!!.value == 0) { // если клетка пустая открываем поле вокруг
                        openFieldByClick(i, k, fieldSizeI, fieldSizeK)
                    }
                }
                if (arrayOfCells[i][k]!!.value == 0 && arrayOfCells[i][k]!!.isBomb == 0) {
                    openFieldByClick(i, k, fieldSizeI, fieldSizeK) // открываем поле без бомб
                }
                if (arrayOfCells[i][k]!!.isBomb == 1) { // если нажали на бомбу игра закончена
                    gameOver(fieldSizeI, fieldSizeK)
                }
            }
        }
    }

    private fun forFun(
        field: LinearLayout,
        switcher: ToggleButton,
        size: LinearLayout.LayoutParams,
        row: LinearLayout.LayoutParams,
        fieldSizeI : Int,
        fieldSizeK : Int
    ) {
        for (i in 0 until fieldSizeI) {
            val linRow = LinearLayout(this)
            for (k in 0 until fieldSizeK) {
                fieldArray[i][k] = ImageView(this)
                fieldArray[i][k]!!.background = cell[4] // задаем изначальный вид поля
                arrayOfCells[i][k]!!.isOpened = false
                numbOfBombs(i, k, fieldSizeI, fieldSizeK)
                if (arrayOfCells[i][k]!!.isEmpty == 1) clickActivity(i, k, switcher, fieldSizeI, fieldSizeK)
                linRow.addView(fieldArray[i][k], size)
            }
            field.addView(linRow, row)
        }
    }

    private fun fieldDesign(fieldSizeI : Int, fieldSizeK : Int) {
        newGame()
        val cellSize = searchScreen() / fieldSizeI
        val row: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(fieldSizeK * cellSize, cellSize)
        val size = LinearLayout.LayoutParams(cellSize, cellSize)
        val switcher = findViewById<View>(R.id.switcher) as ToggleButton
        val field = findViewById<View>(R.id.field) as LinearLayout
        for (i in 0 until fieldSizeI) {
            for (k in 0 until fieldSizeK) {
                arrayOfCells[i][k] = FieldCell()
            }
        }
        var j = 0
        while (j != bombs) {
            val secureRandom = SecureRandom()
            val a = secureRandom.nextInt(fieldSizeI)
            val b = secureRandom.nextInt(fieldSizeK)
            if (arrayOfCells[a][b]!!.isBomb == 0) {
                arrayOfCells[a][b]!!.isBomb = 1
                j ++
            }
        }
        forFun(field, switcher, size, row, fieldSizeI, fieldSizeK)
    }


    private var savedTime: Long = 0
    override fun onPause() {
        super.onPause()
        if (isStarted) chronometer.stop()
        savedTime = SystemClock.elapsedRealtime()
    }

    override fun onRestart() {
        super.onRestart()
        chronometer.base = chronometer.base + (SystemClock.elapsedRealtime() - savedTime) // чтобы сохранялось время при выходе
        chronometer.start()

    }

    private fun searchScreen(): Int {
        return (this.resources.displayMetrics.widthPixels) * 10 // умножаю на 10 чтобы клетки выглядели качественнее
    }
}