package com.example.smart_medical_app.ui.remind

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.smart_medical_app.ClockActivity

class MyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intent:Intent= Intent(context,ClockActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}