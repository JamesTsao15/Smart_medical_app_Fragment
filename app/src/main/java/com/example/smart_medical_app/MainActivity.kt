package com.example.smart_medical_app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smart_medical_app.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var PERMISION_ID=100
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_montior, R.id.navigation_chat, R.id.navigation_remind,R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if(isIgnoringBatteryOptimizations()==false){
            requestIgnoreBatteryOptimizations()
        }
        startForegroundService(Intent(this,MyLocalService::class.java))
        startForegroundService(Intent(this,MyRemoteService::class.java))
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            KeepAliveService.startJob(this)
        }
        if(!checkPermission()){
            requestPermission()
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news")
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            task->
            if(!task.isSuccessful){
                Log.e("JAMES", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token:String=task.result
            Log.e("JAMES","onComplete:"+token)
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
    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)==
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISION_ID)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==PERMISION_ID){
            if(grantResults.isNotEmpty() && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                Log.e("JAMES","You Have the Permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}