package com.example.smart_medical_app.ui.Setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.smart_medical_app.MySettingAdapter
import com.example.smart_medical_app.R
import com.example.smart_medical_app.SettingItem
import com.example.smart_medical_app.custom_UserCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SettingFragment : Fragment() {
    private lateinit var userCardView:custom_UserCardView
    private lateinit var listView_Setting:ListView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser:FirebaseUser
    private lateinit var settingItemArrayList:ArrayList<SettingItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_setting,null)
        settingItemArrayList= ArrayList()
        listView_Setting=root.findViewById(R.id.listView_Setting)
        mAuth= FirebaseAuth.getInstance()
        mUser= mAuth.currentUser!!
        userCardView=root.findViewById(R.id.userCardView)
        userCardView.setUserId("UID:"+mUser.uid)
        userCardView.setUserName(mUser.email!!)
        val item_icon_array=resources.obtainTypedArray(R.array.setting_item_icon)
        val settinglist= listOf<String>("創建群組","加入群組","緊急連絡資訊","緊急電話設定")
        for (i in 0 until item_icon_array.length()){
            val icon=item_icon_array.getResourceId(i,0)
            val name=settinglist[i]
            settingItemArrayList.add(SettingItem(icon,name))
        }
        item_icon_array.recycle()
        listView_Setting.adapter=MySettingAdapter(requireContext(),settingItemArrayList,R.layout.setting_listview_item)
        return root
    }

    override fun onResume() {
        super.onResume()
        listView_Setting.setOnItemClickListener { adapterView, view, position, id ->
            when(position){
                0->
                    findNavController().navigate(R.id.action_navigation_setting_to_navigation_qrcode)
                1->
                    Log.e("JAMES","Join Group")
                2->
                    Log.e("JAMES","emergencyInformation")
                3->
                    Log.e("JAMES","Call")
            }
        }
    }

}