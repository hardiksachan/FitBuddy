package com.hardiksachan.fitbuddy.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.firstRun.FirstRunActivity

class DashboardActivity : AppCompatActivity() {

    val prefs by lazy {
        getSharedPreferences("com.hardiksachan.fitbuddy", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        checkFirstRun()

    }



    private fun checkFirstRun() {
        if (prefs.getBoolean("firstrun", true)) {

            val firstRunIntent = Intent(this, FirstRunActivity::class.java)
            startActivity(firstRunIntent)
            finish()
        }
    }
}