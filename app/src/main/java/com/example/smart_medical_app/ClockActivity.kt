package com.example.smart_medical_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ClockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        val tv_TaskName=findViewById<TextView>(R.id.editText_RemindThing)
        val btn_dismissAlarm=findViewById<Button>(R.id.button_RecieveRemind)
        val btn_WaitAMomentRemind=findViewById<Button>(R.id.button_WaitAMoment)

    }
}