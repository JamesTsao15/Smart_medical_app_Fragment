package com.example.smart_medical_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smart_medical_app.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(Intent(this,MyMQTTService::class.java))
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_montior, R.id.navigation_position, R.id.navigation_remind,R.id.navigation_personal
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if(isIgnoringBatteryOptimizations()==false){
            requestIgnoreBatteryOptimizations()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isIgnoringBatteryOptimizations():Boolean {
        val powerManager:PowerManager=getSystemService(Context.POWER_SERVICE) as PowerManager
        val isIgnoring=powerManager.isIgnoringBatteryOptimizations(packageName)
        return isIgnoring
    }
    private fun requestIgnoreBatteryOptimizations(){
        try{
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
                it.data= Uri.parse("package:$packageName")
                startActivity(it)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}