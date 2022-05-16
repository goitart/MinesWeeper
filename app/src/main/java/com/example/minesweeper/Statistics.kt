package com.example.minesweeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.TextView

class Statistics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        setTimes()
        val resetBtn = findViewById<Button>(R.id.reset)
        resetBtn.setOnClickListener {
            reset()
        }
    }
    fun toMainActivity(view: View) {
        val intent = Intent(this, MainActivity :: class.java)
        finishAndRemoveTask()
        startActivity(intent)
    }
    private fun setTimes() {
        val time1 = findViewById<TextView>(R.id.time1)
        val time2 = findViewById<TextView>(R.id.time2)
        val time3 = findViewById<TextView>(R.id.time3)
        val time4 = findViewById<TextView>(R.id.time4)
        val sharedPreference = getSharedPreferences("ChronometerTime", MODE_PRIVATE)
        val str1 = sharedPreference.getString("Beginner", "-")
        val str2 = sharedPreference.getString("Intermediate", "-")
        val str3 = sharedPreference.getString("Expert", "-")
        val str4 = sharedPreference.getString("Custom", "-")
        time1.text = str1
        time2.text = str2
        time3.text = str3
        time4.text = str4
    }
    private fun reset() {
        val time1 = findViewById<TextView>(R.id.time1)
        val time2 = findViewById<TextView>(R.id.time2)
        val time3 = findViewById<TextView>(R.id.time3)
        val time4 = findViewById<TextView>(R.id.time4)
        val sharedPreference = getSharedPreferences("ChronometerTime", MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("Beginner", "-")
        editor.putString("Intermediate", "-")
        editor.putString("Expert", "-")
        editor.putString("Custom", "-")
        editor.apply()
        time1.text = "-"
        time2.text = "-"
        time3.text = "-"
        time4.text = "-"
    }
}