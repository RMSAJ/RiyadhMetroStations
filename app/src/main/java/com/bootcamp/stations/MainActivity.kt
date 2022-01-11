package com.bootcamp.stations

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bootcamp.stations.profile.ProfileFragmentDirections
import com.bootcamp.stations.user.RegisterFragmentDirections
import com.bootcamp.stations.userPrefrence.SettingsFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private lateinit var auth: FirebaseAuth
 var theme: ImageButton? = null
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        theme?.findViewById<ImageButton>(R.id.settings)?.setOnClickListener{
            gotoSettings()
        }

        val navHostFragment =supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

      val bottomNavigationView = findViewById<BottomNavigationView> (R.id.bottom_navigation)

        bottomNavigationView.setupWithNavController(navController)
//               setupActionBarWithNavController(navController)
    }

    private fun gotoSettings(){

        findNavController(R.id.action_favoriteFragment_to_settingsFragment)

        findNavController(R.id.action_favoriteFragment_to_settingsFragment)

        findNavController(R.id.action_favoriteFragment_to_settingsFragment)
    }



//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}