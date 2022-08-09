package com.example.smart_medical_app

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_medical_app.ui.remind.AlarmTime
import org.w3c.dom.Text

class MyAdapter(private val data:ArrayList<AlarmTime>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val tv_taskName=v.findViewById<TextView>(R.id.textView_viewTaskName)
        val tv_SettingTime=v.findViewById<TextView>(R.id.textView_SettingTime)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val sw_setOrCancel=v.findViewById<Switch>(R.id.switch_SetOrCancel)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyAdapter.ViewHolder {
        val v=LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.alarm_list_item,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeString:String= String.format("%02d:%02d",data[position].Hour,data[position].Minute)
        holder.tv_SettingTime.text=timeString
        holder.tv_taskName.text=data[position].Task
        holder.sw_setOrCancel.isChecked=true
        holder.sw_setOrCancel.setOnCheckedChangeListener { Switch, isChecked ->
            Log.e("JAMES","switch is ${isChecked}")
        }
    }

    override fun getItemCount(): Int=data.size


}