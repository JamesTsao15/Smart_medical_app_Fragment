package com.example.smart_medical_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_medical_app.ui.remind.AlarmTime

class MyAdapter: RecyclerView.Adapter<MyAdapter.mViewHolder>() {

    var unAssignList = listOf<AlarmTime>()

    inner class mViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_SettingTime = itemView.findViewById<TextView>(R.id.textView_SettingTime)
        val tv_taskname = itemView.findViewById<TextView>(R.id.textView_viewTaskName)
        fun bind(item: AlarmTime){
            //綁定當地變數與dataModel中的每個值
            tv_SettingTime.text=item.Hour.toString()+":"+item.Minute.toString()
            tv_taskname.text = item.Task

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.alarm_list_item,parent,false)
        view.findViewById<Switch>(R.id.switch_SetOrCancel).isChecked=true
        return mViewHolder(view)
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        holder.bind(unAssignList[position])
    }

    override fun getItemCount(): Int =unAssignList.size
    fun updateList(list:ArrayList<AlarmTime>){
        unAssignList=list
    }
}