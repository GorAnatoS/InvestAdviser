package com.invest.advisor.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.invest.advisor.R

class MainActivity : AppCompatActivity() {

    lateinit var myContext: Context

    private lateinit var navController: NavController
    val appBarConfiguration = AppBarConfiguration(setOf(
        R.id.portfolioFragment,
        R.id.moexFragment,
        R.id.analiticsFragment
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        myContext = applicationContext

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this,
            R.id.nav_host_fragment
        )

        val bottomNav: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNav.setupWithNavController(navController)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //setupActionBarWithNavController(navController, appBarConfiguration)

        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}

// TODO: 9/20/2020 разобраться со временем


// TODO: 10/21/2020 функция удаления в портфолио
//аналиитку -Ю рекоментадии