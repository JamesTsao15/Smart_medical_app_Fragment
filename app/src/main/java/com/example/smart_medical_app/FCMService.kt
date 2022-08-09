package com.example.smart_medical_app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("JAMES","裝置token"+token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title:String= message.notification?.title ?: ""
        val text:String= message.notification?.body ?:""
        val channel_ID:String="FCM_channel"
        val channel:NotificationChannel=NotificationChannel(channel_ID,"Notification",NotificationManager.IMPORTANCE_HIGH)
        val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification:Notification=Notification.Builder(this,channel_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true).build()
        notificationManager.createNotificationChannel(channel)
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock:PowerManager.WakeLock=powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,"appname::WakeLock")
        wakeLock.acquire(1*60*1000L)
        NotificationManagerCompat.from(this).notify(2,notification)
    }

}