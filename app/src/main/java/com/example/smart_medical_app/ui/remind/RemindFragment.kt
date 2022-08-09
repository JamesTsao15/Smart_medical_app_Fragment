package com.example.smart_medical_app.ui.remind

import android.app.*
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
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
import com.example.smart_medical_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.annotations.Until
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
    private lateinit var progressDialog: ProgressDialog
    private lateinit var numberPicker_Hour: NumberPicker
    private lateinit var numberPicker_Minute: NumberPicker
    private lateinit var recycleView:RecyclerView
    private lateinit var custom_DialogView:View
    private lateinit var dayPicker:LinearLayout
    private lateinit var setRemindDayOfWeek:ArrayList<Boolean>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var settingAlarmTimeList:ArrayList<AlarmTime>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var adapter: MyAdapter
    private var uid:String=""
    private var hour:Int=-1
    private var minute:Int=-1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root=inflater.inflate(R.layout.fragment_remind,null)
        val inflater_view=LayoutInflater.from(requireContext())
        settingAlarmTimeList= ArrayList()
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
        progressDialog=ProgressDialog(requireContext())
        mAuth= FirebaseAuth.getInstance()
        uid=mAuth.currentUser?.uid ?:""
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_RemindTask")
        btn_add=root.findViewById(R.id.btn_add)
        val linearLayoutManager=LinearLayoutManager(requireContext())
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        recycleView.layoutManager=linearLayoutManager
        adapter= MyAdapter(settingAlarmTimeList)
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
                        showProgressDialog()
                        storeDataToRealTimeDatabase(AlarmTime(taskName,Remindcycle,hour,minute))
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
        databaseReference.child(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                settingAlarmTimeList.clear()
                for(data in snapshot.children){
                    val taskName=data.child("task").getValue().toString()
                    val remindCycle=data.child("remindCycle").getValue().toString()
                    val minute=data.child("minute").getValue().toString().toInt()
                    val hour=data.child("hour").getValue().toString().toInt()
                    val alarmTime=AlarmTime(taskName,remindCycle,hour,minute)
                    settingAlarmTimeList.add(alarmTime)
                    Log.e("JAMES",alarmTime.toString())
                    adapter.notifyDataSetChanged()
                    recycleView.adapter=adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun storeDataToRealTimeDatabase(alarmTime: AlarmTime) {
        if(uid!=""){
            databaseReference.child(uid).child(alarmTime.Task).setValue(alarmTime).addOnCompleteListener {
                    task->
                if(task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"資料儲存成功",Toast.LENGTH_SHORT).show()
                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),"資料儲存失敗",Toast.LENGTH_SHORT).show()
                }
            }
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
    private fun showProgressDialog() {
        progressDialog.setMessage("正在儲存中，請稍後...")
        progressDialog.setTitle("儲存資料")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }


}