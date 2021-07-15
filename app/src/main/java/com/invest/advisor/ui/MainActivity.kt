package com.invest.advisor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.invest.advisor.R

/*
MainActivity class where we setting up navigationController component and bottomNavigationBar
 */

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.portfolioFragment,
            R.id.moexFragment,
            R.id.recommendationsFragment
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )

        val bottomNav: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}