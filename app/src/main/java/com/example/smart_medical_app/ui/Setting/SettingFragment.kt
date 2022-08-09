package com.example.smart_medical_app.ui.Setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.smart_medical_app.MySettingAdapter
import com.example.smart_medical_app.R
import com.example.smart_medical_app.SettingItem
import com.example.smart_medical_app.custom_UserCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SettingFragment : Fragment() {
    private lateinit var userCardView:custom_UserCardView
    private lateinit var listView_Setting:ListView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser:FirebaseUser
    private lateinit var btn_SendUserID:Button
    private lateinit var editText_UserID: EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var MyDialog: Dialog
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
        databaseReference=FirebaseDatabase.getInstance().getReference("Users_ID")
        userCardView=root.findViewById(R.id.userCardView)
        userCardView.setUserId("UID:"+mUser.uid)
        userCardView.setUserName(mUser.email!!)
        val item_icon_array=resources.obtainTypedArray(R.array.setting_item_icon)
        val settinglist= listOf<String>("基本資料","新增設備","緊急連絡資訊","緊急電話設定")
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
                    showDialog()
                1->
                    Log.e("JAMES","addMonitor")
                2->
                    findNavController().navigate(R.id.action_navigation_setting_to_navigation_emergencyInfo)
                3->
                    Log.e("JAMES","Call")
            }
        }
    }

    private fun showDialog() {
        MyDialog= Dialog(requireContext())
        MyDialog.setContentView(R.layout.custom_user_id_input)
        btn_SendUserID=MyDialog.findViewById(R.id.button_sendUserID)
        editText_UserID=MyDialog.findViewById(R.id.editTextUserID)
        btn_SendUserID.isEnabled=true
        btn_SendUserID.setOnClickListener {
            Log.e("JAMES","sendUserID")
            val userId=editText_UserID.text.toString()
            storeUserIDToRealtimeDatabase(userId)
            MyDialog.dismiss()
        }
        MyDialog.show()
    }

    private fun storeUserIDToRealtimeDatabase(userID:String) {
        databaseReference.child(mUser.uid).setValue(userID).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                Toast.makeText(requireContext(),"儲存UserID成功",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),"儲存UserID失敗",Toast.LENGTH_SHORT).show()
            }
        }
    }

}