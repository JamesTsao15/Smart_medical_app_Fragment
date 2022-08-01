package com.example.smart_medical_app

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils

class ServiceUtils(val context: Context, val serviceName: String) {
    fun isServiceRunning():Boolean{
        if(TextUtils.isEmpty(serviceName)) return false
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = activityManager.getRunningServices(200)
        for (info in infos) {
            if (TextUtils.equals(info.service.className, serviceName)) {
                return true
            }
        }
        return false
    }
}