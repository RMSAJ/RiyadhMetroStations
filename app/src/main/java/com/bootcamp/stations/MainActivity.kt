package com.bootcamp.stations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController
import com.bootcamp.stations.userPrefrence.ThemeApplication

import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private lateinit var auth: FirebaseAuth
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ThemeApplication()
        val navHostFragment =supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

      val bottomNavigationView = findViewById<BottomNavigationView> (R.id.bottom_navigation)

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.registerFragment || destination.id == R.id.signInFragment ) {
                bottomNavigationView.visibility = View.GONE
            } else bottomNavigationView.visibility = View.VISIBLE
        }

//               setupActionBarWithNavController(navController)
    }

    private fun checwhechFragment(){


    }



//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}