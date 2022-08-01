package com.example.smart_medical_app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.smart_medical_app.ui.remind.AlarmTime
import java.util.*
import kotlin.collections.ArrayList

class MyAlarmService : Service() {
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private val CHANNEL_2_ID="BackgroundChannel2"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Log.e("JAMES","AlarmServiceOnCreate")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder ?=null
}