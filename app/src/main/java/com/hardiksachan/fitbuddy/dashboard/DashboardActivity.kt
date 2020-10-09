package com.hardiksachan.fitbuddy.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.firstRun.FirstRunActivity

class DashboardActivity : AppCompatActivity() {

    val prefs by lazy {
        getSharedPreferences("com.hardiksachan.fitbuddy", MODE_PRIVATE)
    }

    lateinit var drawerLayout: DrawerLayout

    val appBarConfig by lazy {
        AppBarConfiguration(
            setOf(
                R.id.mainDashboardFragment,
                R.id.exerciseByDayFragment
            ),
            drawerLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navController = this.findNavController(R.id.dashboard_nav_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig)
        NavigationUI.setupWithNavController(
            findViewById<NavigationView>(R.id.nav_view),
            navController
        )

        checkFirstRun()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.dashboard_nav_fragment)
        return NavigationUI.navigateUp(navController, appBarConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController(R.id.dashboard_nav_fragment))
                || super.onOptionsItemSelected(item)

    private fun checkFirstRun() {
        if (prefs.getBoolean("firstrun", true)) {

            val firstRunIntent = Intent(this, FirstRunActivity::class.java)
            startActivity(firstRunIntent)
            finish()
        }
    }
}