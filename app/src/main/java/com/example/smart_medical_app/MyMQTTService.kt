package com.example.smart_medical_app

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MyMQTTService : Service() {

    private lateinit var mqttClient: MqttAndroidClient
    private lateinit var fell_down_notification: Notification
    private lateinit var notificationManager: NotificationManager
    private fun MQTT_connect(context: Context){
        val serverURI="ssl://95cdf9091ac24bf5931fb43b3260cac8.s1.eu.hivemq.cloud:8883"
        var recCount=0
        mqttClient= MqttAndroidClient(context,serverURI,"kotlin_client")
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                recCount = recCount + 1
                Log.e("JAMES", "Received message ${recCount}: ${message.toString()} from topic: $topic")
                if (message.toString()=="fell_Down"){
                    notificationManager.notify(0,fell_down_notification)
                }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e("JAMES", "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
        val mqttoptions = MqttConnectOptions()
        mqttoptions.userName = "JamesTsao"
        mqttoptions.password = "Jj0928338296".toCharArray()
        try {
            mqttClient.connect(mqttoptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("JAMES", "Connection success")
                    MQTT_subscribe("medical/#")
                    MQTT_publish("medical/fell_down_alarm", "cellphone_connect")

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("JAMES", "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun MQTT_subscribe(topic: String,qos:Int=1) {
        try {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("JAMES", "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("JAMES", "Failed to subscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun MQTT_publish(topic: String, msg: String,qos:Int=1,retained:Boolean=false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("JAMES", "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("JAMES", "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun MQTT_disconnect(){
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e("JAMES", "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("JAMES", "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun notification_Setting(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel= NotificationChannel("fell_down_alarm","Fell_Down_Alarm",
                NotificationManager.IMPORTANCE_HIGH)
            val fell_down_builder = Notification.Builder(this,"fell_down_alarm")
            val intent=Intent(this,MainActivity::class.java)
            val pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
            fell_down_builder.setSmallIcon(R.drawable.ic_baseline_medical_services_24)
                .setContentTitle("緊急通知")
                .setContentText("有人跌倒了，請注意監控")
                .setWhen(System.currentTimeMillis())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            fell_down_notification=fell_down_builder.build()
            notificationManager=getSystemService(NOTIFICATION_SERVICE)as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onCreate() {
        super.onCreate()
        Log.e("JAMES","MQTTServiceOnCreate")
        MQTT_connect(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notification_Setting()
        Log.e("JAMES","startMQTTService")
        val channel= NotificationChannel("MQTT_Service","Start_MQTT_Listener",
            NotificationManager.IMPORTANCE_HIGH)
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }
        val notification: Notification =Notification.Builder(this,"MQTT_Service")
            .setContentTitle("「Smart_Medical_APP」目前正在執行中")
            .setContentText("輕觸即可嘹解詳情或停止應用程式")
            .setSmallIcon(R.drawable.ic_baseline_medical_services_24)
            .setContentIntent(pendingIntent)
            .setTicker("Smart_Medical_APP」啟動")
            .build()
        startForeground(1,notification)
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder ?=null

    override fun onDestroy() {
        MQTT_disconnect()
        super.onDestroy()
    }
}