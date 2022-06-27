package com.example.smart_medical_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.example.smart_medical_app.ui.remind.AlarmTime
import java.util.*
import kotlin.collections.ArrayList

class ClockActivity : AppCompatActivity() {
    private var SettingAlarmTime:ArrayList<AlarmTime> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        val arrayList_in_sharedpreferrence
                =Store_and_Load_ArrayList(this,"MedicalApp",
            SettingAlarmTime,"Alarm_ArrayList")
        SettingAlarmTime = arrayList_in_sharedpreferrence.loadData()
        val tv_TaskName=findViewById<TextView>(R.id.textView_RemindThing)
        val btn_dismissAlarm=findViewById<Button>(R.id.button_RecieveRemind)
        val btn_WaitAMomentRemind=findViewById<Button>(R.id.button_WaitAMoment)
        for(i in 0 until SettingAlarmTime.size){
            val currentTime: Calendar= Calendar.getInstance()
            val alarmTimeData:AlarmTime=SettingAlarmTime[i]
            val task:String=alarmTimeData.Task
            val hour:Int=alarmTimeData.Hour
            val minute:Int=alarmTimeData.Minute
            if(hour==currentTime.get(Calendar.HOUR_OF_DAY) && minute==currentTime.get(Calendar.MINUTE)){
                SettingAlarmTime.removeAt(i)
                Log.e("JAMES",SettingAlarmTime.toString())
                tv_TaskName.text=task
            }
        }
        btn_dismissAlarm.setOnClickListener {
            finish()
        }
        Log.e("JAMES",SettingAlarmTime.toString())
    }

    override fun finish() {
        super.finish()
        val intent:Intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}