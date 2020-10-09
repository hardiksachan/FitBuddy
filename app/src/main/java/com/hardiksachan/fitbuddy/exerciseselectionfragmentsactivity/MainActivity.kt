package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.hardiksachan.fitbuddy.R

class MainActivity : AppCompatActivity() {

    val prefs by lazy {
        getSharedPreferences("com.hardiksachan.fitbuddy", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.my_nav_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}