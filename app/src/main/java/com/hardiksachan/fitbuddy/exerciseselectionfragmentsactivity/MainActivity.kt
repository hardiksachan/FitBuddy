package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hardiksachan.fitbuddy.R

class MainActivity : AppCompatActivity() {

    val prefs by lazy {
        getSharedPreferences("com.hardiksachan.fitbuddy", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}