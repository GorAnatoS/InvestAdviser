package com.invest.advisor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.invest.advisor.R


/**
 * MainActivity class where we setting up navigationController and bottomNavigationBar
 */

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.portfolioFragment,
            R.id.moexSecuritiesListFragment
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = getNavController()

        val bottomNav: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun getNavController(): NavController {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        check(fragment is NavHostFragment) {
            ("Activity " + this
                    + " does not have a NavHostFragment")
        }
        return (fragment as NavHostFragment?)!!.navController
    }
}