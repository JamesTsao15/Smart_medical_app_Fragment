package com.example.smart_medical_app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.smart_medical_app.ui.remind.AlarmTime
import java.util.*
import kotlin.collections.ArrayList

class MyAlarmService : Service() {
    private lateinit var currentTime:Calendar
    private lateinit var SettingAlarmTime:ArrayList<AlarmTime>

    override fun onCreate() {
        super.onCreate()

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder ?=null
}