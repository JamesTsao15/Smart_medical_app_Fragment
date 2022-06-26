package com.example.smart_medical_app.ui.remind

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.smart_medical_app.ClockActivity
import com.example.smart_medical_app.MainActivity
import com.example.smart_medical_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class RemindFragment : Fragment() {
    private lateinit var floatingActionButton:FloatingActionButton
    private lateinit var spinner_RemindCycle:Spinner
    private lateinit var numberPicker_Hour:NumberPicker
    private lateinit var numberPicker_Minute:NumberPicker
    private lateinit var alarmManager:AlarmManager
    private var SettingAlarmTime:ArrayList<AlarmTime> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root=inflater.inflate(R.layout.fragment_remind,null)
        floatingActionButton=root.findViewById(R.id.floatingActionButton_add)
        return root
    }
    private fun viewSetUp(){
        val hourArrayList:ArrayList<String> = ArrayList()
        val minuteArrayList:ArrayList<String> = ArrayList()
        val arrayAdapter:ArrayAdapter<CharSequence> = ArrayAdapter
            .createFromResource(requireContext(),R.array.Remind_choose,android.R.layout.simple_spinner_item)
        for (i in 0 .. 23){
            hourArrayList.add(String.format("%02d",i))
        }
        for (i in 0 .. 59){
            minuteArrayList.add(String.format("%02d",i))
        }
        spinner_RemindCycle.adapter=arrayAdapter
        spinner_RemindCycle.setSelection(0)
        numberPicker_Minute.maxValue=59
        numberPicker_Minute.minValue=0
        numberPicker_Minute.displayedValues=minuteArrayList.toTypedArray()
        numberPicker_Hour.maxValue=23
        numberPicker_Hour.minValue=0
        numberPicker_Hour.displayedValues=hourArrayList.toTypedArray()
    }
    private fun setAlarmTime(alarmInformation:AlarmTime) {
        val intent:Intent=Intent(requireContext(),ClockActivity::class.java)
        val pendingIntent:PendingIntent= PendingIntent
            .getActivity(requireContext(),0,intent,PendingIntent.FLAG_IMMUTABLE)
        val c:Calendar= Calendar.getInstance()
        c.timeInMillis=System.currentTimeMillis()
        c.set(Calendar.HOUR,alarmInformation.Hour)
        c.set(Calendar.MINUTE,alarmInformation.Minute)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            alarmManager.set(AlarmManager.RTC_WAKEUP,c.timeInMillis,pendingIntent)
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        }
        Toast.makeText(requireContext(),"提醒設置完畢",Toast.LENGTH_SHORT).show()

    }
    override fun onResume() {
        super.onResume()
        var SettingAlarmHour:Int=-1
        var SettingAlarmMinute:Int=-1
        var RemindCycle:Int=0
        alarmManager=requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        floatingActionButton.setOnClickListener {
            Log.e("JAMES","add_click")
            val inflater=LayoutInflater.from(requireContext())
            val builder=AlertDialog.Builder(requireContext())
            val view=inflater.inflate(R.layout.dialog_setting_remind,null)
            val editText_RemindThing:EditText=view.findViewById(R.id.editText_RemindThing)
            spinner_RemindCycle=view.findViewById(R.id.spinner_RemindCycle)
            numberPicker_Hour=view.findViewById(R.id.numberPicker_Hour)
            numberPicker_Minute=view.findViewById(R.id.numberPicker_Minute)
            viewSetUp()
            val currentTime:Calendar= Calendar.getInstance()
            numberPicker_Hour.value=currentTime.get(Calendar.HOUR_OF_DAY)
            numberPicker_Minute.value=currentTime.get(Calendar.MINUTE)
            spinner_RemindCycle.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    RemindCycle=position
                    Log.e("JAMES","position:$position")
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e("JAMES","position:0")
                }

            }
            numberPicker_Hour.setOnValueChangedListener {
                    numberPicker, oldval, newval ->
                SettingAlarmHour=newval
            }
            numberPicker_Minute.setOnValueChangedListener {
                    numberPicker, oldval, newval ->
                SettingAlarmMinute=newval
            }
            builder.setTitle("設定提醒時間")
                   .setView(view)
                   .setPositiveButton("確定"){
                       dialog,which->
                       if(editText_RemindThing.text.toString()!=""){
                           val task=editText_RemindThing.text.toString()
                           SettingAlarmTime.add(AlarmTime(task,RemindCycle,SettingAlarmHour,SettingAlarmMinute))
                           setAlarmTime(AlarmTime(task,RemindCycle,SettingAlarmHour,SettingAlarmMinute))
                           Log.e("JAMES",SettingAlarmTime.toString())
                       }
                       else{
                           Toast.makeText(requireContext(),"提醒項目欄不得為空，請重新輸入",Toast.LENGTH_SHORT).show()
                       }
                   }
                   .setNegativeButton("取消"){
                       dialog,which->

                   }.show()

        }

    }


}