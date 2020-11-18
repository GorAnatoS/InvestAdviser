package com.invest.advisor.ui

import android.content.Context
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
        R.id.recommendationsFragment
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


// TODO: 11/17/2020 Для публикации осталось:
// TODO: 11/17/2020 firebase add
// TODO: 11/17/2020 icon + screens


//интересные библиотки

// range bars
//https://github.com/warkiz/IndicatorSeekBar
//https://github.com/MarcinMoskala/ArcSeekBar
//https://github.com/harjot-oberai/Croller
//https://github.com/stfalcon-studio/StfalconPriceRangeBar-android

//время, выбор дат из календаря
//https://github.com/square/android-times-square

// TODO: 9/20/2020 разобраться со временем


// TODO: 11/16/2020 styling 
//аналиитку -Ю рекоментадии