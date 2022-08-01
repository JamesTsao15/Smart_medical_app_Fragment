package com.example.smart_medical_app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi

class MyLocalService : Service() {
    private lateinit var myBinder:MyBinder
    private lateinit var connection: Connection
    private val CHANNEL_1_ID:String="KeepAliveChannel"

    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bind_service()
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun bind_service() {
        connection=Connection()
        val bindIntent:Intent=Intent(this,MyRemoteService::class.java)
        bindService(bindIntent,connection, BIND_AUTO_CREATE)
    }

    override fun onCreate() {
        super.onCreate()
        myBinder= MyBinder()
        runNoti()
    }

    fun runNoti() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel: NotificationChannel = NotificationChannel(CHANNEL_1_ID,
                "keepAlive", NotificationManager.IMPORTANCE_NONE)
            channel.lightColor= Color.BLUE
            channel.lockscreenVisibility= Notification.VISIBILITY_PRIVATE
            val service: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            service.createNotificationChannel(channel)
            val notification: Notification = Notification.Builder(applicationContext,CHANNEL_1_ID).build()
            startForeground(10,notification)
        }
        else if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR2){
            startForeground(10, Notification())
            startService(Intent(this,CancelNotificationService::class.java))
        }
        else if (Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR2){
            startForeground(10, Notification())
        }
    }
    inner class Connection: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onServiceDisconnected(name: ComponentName?) {
            runNoti()
            bind_service()
        }

    }
    inner class CancelNotificationService :Service(){
        override fun onCreate() {
            super.onCreate()
            startForeground(10, Notification())
            stopSelf()
        }
        override fun onBind(intent: Intent?): IBinder? {
            return null
        }

    }

}