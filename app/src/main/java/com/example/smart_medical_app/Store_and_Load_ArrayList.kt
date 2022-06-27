package com.example.smart_medical_app

import android.content.Context
import android.content.SharedPreferences
import com.example.smart_medical_app.ui.remind.AlarmTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Store_and_Load_ArrayList
    (val context: Context,var sharedPreferences_name:String,var _arrayList:ArrayList<AlarmTime>,
     var store_arrayList_Name:String)
{
        fun saveData(){
            val sharedPreferences=context.getSharedPreferences(sharedPreferences_name,Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor=sharedPreferences.edit()
            val gson:Gson= Gson()
            val json:String=gson.toJson(_arrayList)
            editor.putString(store_arrayList_Name,json)
            editor.apply()
        }
        fun loadData():ArrayList<AlarmTime>
        {
            val sharedPreferences=context.getSharedPreferences(sharedPreferences_name,Context.MODE_PRIVATE)
            val gson:Gson= Gson()
            val json:String=sharedPreferences.getString(store_arrayList_Name,null)!!
            val type:Type=object: TypeToken<ArrayList<AlarmTime>>(){}.type
            val arrayList=gson.fromJson<ArrayList<AlarmTime>>(json,type)
            if(arrayList==null){
                val EmptyArrayList:ArrayList<AlarmTime> = ArrayList()
                return EmptyArrayList
            }
            else{
                return arrayList
            }
        }

}