package com.example.smart_medical_app.ui.remind

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_medical_app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.path.createTempDirectory

class RemindFragment : Fragment() {
    private lateinit var editText_RemindThing:EditText
    private lateinit var btn_add:Button
    private lateinit var tgButton_Sun: ToggleButton
    private lateinit var tgButton_Mon: ToggleButton
    private lateinit var tgButton_Tue: ToggleButton
    private lateinit var tgButton_Wen: ToggleButton
    private lateinit var tgButton_Thu: ToggleButton
    private lateinit var tgButton_Fri: ToggleButton
    private lateinit var tgButton_Sat: ToggleButton
    private lateinit var numberPicker_Hour: NumberPicker
    private lateinit var numberPicker_Minute: NumberPicker
    private lateinit var recycleView:RecyclerView
    private lateinit var custom_DialogView:View
    private lateinit var dayPicker:LinearLayout
    private lateinit var setRemindDayOfWeek:ArrayList<Boolean>
    private lateinit var SettingAlarmTimeArrayList:ArrayList<AlarmTime>
    private lateinit var sharedPreferencesArrayList:ArrayList<AlarmTime>
    private lateinit var adapter: MyAdapter
    private var hour:Int=-1
    private var minute:Int=-1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root=inflater.inflate(R.layout.fragment_remind,null)
        val inflater_view=LayoutInflater.from(requireContext())
        SettingAlarmTimeArrayList= ArrayList()
        custom_DialogView=inflater_view.inflate(R.layout.dialog_setting_remind,null)
        editText_RemindThing=custom_DialogView.findViewById(R.id.editText_RemindThing)
        dayPicker=custom_DialogView.findViewById(R.id.dayPicker)
        recycleView=root.findViewById(R.id.recycleView)
        tgButton_Sun=dayPicker.findViewById(R.id.tSun)
        tgButton_Mon=dayPicker.findViewById(R.id.tMon)
        tgButton_Tue=dayPicker.findViewById(R.id.tTues)
        tgButton_Wen=dayPicker.findViewById(R.id.tWen)
        tgButton_Thu=dayPicker.findViewById(R.id.tThurs)
        tgButton_Fri=dayPicker.findViewById(R.id.tFri)
        tgButton_Sat=dayPicker.findViewById(R.id.tSat)
        numberPicker_Hour=custom_DialogView.findViewById(R.id.numberPicker_Hour)
        numberPicker_Minute=custom_DialogView.findViewById(R.id.numberPicker_Minute)
        btn_add=root.findViewById(R.id.btn_add)
        try{
            sharedPreferencesArrayList=Store_and_Load_ArrayList(requireContext(),"UserSettingInformation", SettingAlarmTimeArrayList,"AlarmTimeArrayList").loadData()
            SettingAlarmTimeArrayList=sharedPreferencesArrayList
        }catch (e:NullPointerException){
            e.printStackTrace()
        }
        val linearLayoutManager=LinearLayoutManager(requireContext())
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        recycleView.layoutManager=linearLayoutManager
        adapter= MyAdapter(SettingAlarmTimeArrayList)
        recycleView.adapter=adapter
        return root
    }
    override fun onResume() {
        super.onResume()
        var Hour:Int=-1
        var Minute:Int=-1
        alertDialogViewSetUp()
        setRemindDayOfWeek=ArrayList()
        for (i in 0 .. 6){
            setRemindDayOfWeek.add(false)
        }
        btn_add.setOnClickListener {
            editText_RemindThing.text.clear()
            tgButton_Sun.isChecked=false
            tgButton_Mon.isChecked=false
            tgButton_Tue.isChecked=false
            tgButton_Wen.isChecked=false
            tgButton_Thu.isChecked=false
            tgButton_Fri.isChecked=false
            tgButton_Sat.isChecked=false
           val builder:AlertDialog.Builder=AlertDialog.Builder(requireContext())
            builder.setTitle("設定提醒")
                .setView(custom_DialogView)
                .setNegativeButton("取消"){
                    dialog,which->

                }
                .setPositiveButton("確定"){
                    dialog,which->
                    if(editText_RemindThing.text.toString()!=""){
                        var Remindcycle:String=""
                        val taskName=editText_RemindThing.text.toString()
                        for(i in 0 .. 6){
                            if(setRemindDayOfWeek[i]==true){

                                if(i==6){
                                    Remindcycle+="true"
                                }
                                else{
                                    Remindcycle+="true,"
                                }

                            }
                            else{
                                if(i==6){
                                    Remindcycle+="false"
                                }
                                else{
                                    Remindcycle+="false,"
                                }

                            }
                        }
                        Log.e("JAMES",Remindcycle)
                        SettingAlarmTimeArrayList.add(AlarmTime(taskName,Remindcycle,hour,minute))
                        Log.e("JAMES",SettingAlarmTimeArrayList.toString())
                        Store_and_Load_ArrayList(requireContext(),"UserSettingInformation",
                            SettingAlarmTimeArrayList,"AlarmTimeArrayList").saveData()
                        adapter.notifyDataSetChanged()
                    }
                }
                .setOnDismissListener {
                    (custom_DialogView.parent as ViewGroup).removeView(custom_DialogView)
                }
                .show()
        }
        tgButton_Sun.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Sun is Checked")
            setRemindDayOfWeek.set(0,ischecked)
        }
        tgButton_Mon.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Mon is Checked")
            setRemindDayOfWeek.set(1,ischecked)
        }
        tgButton_Tue.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Tue is Checked")
            setRemindDayOfWeek.set(2,ischecked)
        }
        tgButton_Wen.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Wen is Checked")
            setRemindDayOfWeek.set(3,ischecked)
        }
        tgButton_Thu.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Thu is Checked")
            setRemindDayOfWeek.set(4,ischecked)
        }
        tgButton_Fri.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Fri is Checked")
            setRemindDayOfWeek.set(5,ischecked)
        }
        tgButton_Sat.setOnCheckedChangeListener { buttonView, ischecked ->
            Log.e("JAMES","Sat is Checked")
            setRemindDayOfWeek.set(6,ischecked)
        }
        numberPicker_Hour.setOnValueChangedListener { numberPicker, oldval, newval ->
            hour=newval
            Log.e("JAMES",newval.toString())
        }
        numberPicker_Minute.setOnValueChangedListener { numberPicker, oldval , newval ->
            minute=newval
            Log.e("JAMES",newval.toString())
        }
    }

    private fun alertDialogViewSetUp() {
        val hourArrayList:ArrayList<String> = ArrayList()
        val minuteArrayList:ArrayList<String> = ArrayList()
        for (i in 0 .. 23){
            hourArrayList.add(String.format("%02d",i))
        }
        for (i in 0 .. 59){
            minuteArrayList.add(String.format("%02d",i))
        }
        numberPicker_Minute.maxValue=59
        numberPicker_Minute.minValue=0
        numberPicker_Minute.displayedValues=minuteArrayList.toTypedArray()
        numberPicker_Hour.maxValue=23
        numberPicker_Hour.minValue=0
        numberPicker_Hour.displayedValues=hourArrayList.toTypedArray()
        val currentTime:Calendar= Calendar.getInstance()
        hour=currentTime.get(Calendar.HOUR_OF_DAY)
        minute=currentTime.get(Calendar.MINUTE)
        numberPicker_Hour.value=currentTime.get(Calendar.HOUR_OF_DAY)
        numberPicker_Minute.value=currentTime.get(Calendar.MINUTE)
    }


}