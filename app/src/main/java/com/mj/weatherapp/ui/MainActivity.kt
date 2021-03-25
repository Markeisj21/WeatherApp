package com.mj.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mj.weatherapp.*
import com.mj.weatherapp.forecast.CurrentForecastFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){


    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tempDisplaySettingManager = TempDisplaySettingManager(this)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setTitle(R.string.app_name)
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setupWithNavController(navController)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.tempDisplaySetting -> {
                showTempDisplaySettingDialog(this,tempDisplaySettingManager)
                true

            }else -> super.onOptionsItemSelected(item)
        }
    }






}