package com.example.smart_medical_app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MySettingAdapter(context: Context,data:ArrayList<SettingItem>,
    private val layout:Int)
    :ArrayAdapter<SettingItem>(context,layout,data){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view=View.inflate(parent.context,layout,null)
        val item=getItem(position)?:return view
        val img_icon=view.findViewById<ImageView>(R.id.imageView_icon)
        img_icon.setImageResource(item.icon)
        val tv_setting_item_name=view.findViewById<TextView>(R.id.tv_setting_Name)
        tv_setting_item_name.text=item.SettingItem
        return view
    }
}