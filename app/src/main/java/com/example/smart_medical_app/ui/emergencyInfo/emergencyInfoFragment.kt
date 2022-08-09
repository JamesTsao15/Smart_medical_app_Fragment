package com.example.smart_medical_app.ui.emergencyInfo

import android.R.attr.x
import android.R.attr.y
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smart_medical_app.R
import com.example.smart_medical_app.UserEmergencyInfo
import com.example.smart_medical_app.customView_emergencyInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class emergencyInfoFragment : Fragment() {
    private lateinit var button_add_emergencyInfo:FloatingActionButton
    private lateinit var mAuth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var CustomviewEmergencyinfo: customView_emergencyInfo
    private lateinit var btn_setTheWallPaper:Button
    private var uid:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_emergency_info, null)
        button_add_emergencyInfo=root.findViewById(R.id.floatingActionButton_addEmergencyInfo)
        CustomviewEmergencyinfo=root.findViewById(R.id.customView_emregnecyInfo)
        mAuth= FirebaseAuth.getInstance()
        uid=mAuth.currentUser?.uid ?:""
        btn_setTheWallPaper=root.findViewById(R.id.button_setTheWallPaper)
        progressDialog=ProgressDialog(requireContext())
        databaseReference=FirebaseDatabase.getInstance().getReference("Users_EmergencyInformation")
        return root
    }

    override fun onResume() {
        super.onResume()
        button_add_emergencyInfo.setOnClickListener {
            settingAlertDialog()
        }
        databaseReference.child(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val emergencyInfo=snapshot.getValue<UserEmergencyInfo>()
                emergencyInfo!!.personName?.let { CustomviewEmergencyinfo.setPersonName(it) }
                emergencyInfo.healthStatus?.let { CustomviewEmergencyinfo.setHealthStatus(it) }
                emergencyInfo.contactPerson1?.let { CustomviewEmergencyinfo.setContactUser1(it) }
                emergencyInfo.contactPhone1?.let { CustomviewEmergencyinfo.setContactPhone1(it) }
                emergencyInfo.contactPerson2?.let { CustomviewEmergencyinfo.setContactUser2(it) }
                emergencyInfo.contactPhone2?.let { CustomviewEmergencyinfo.setContactPhone2(it) }
                emergencyInfo.address?.let { CustomviewEmergencyinfo.setAddress(it) }
                Log.e("JAMES",emergencyInfo.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"載入失敗，$error",Toast.LENGTH_SHORT).show()
            }

        })
        btn_setTheWallPaper.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_emergencyInfo_to_navigation_WallPaper)
        }
    }

    private fun settingAlertDialog() {
        val inflater_view=LayoutInflater.from(requireContext())
        val view=inflater_view.inflate(R.layout.dialog_view_emergency_info,null)
        val editText_PersonName=view.findViewById<EditText>(R.id.editText_PersonName)
        val editText_health=view.findViewById<EditText>(R.id.editText_health)
        val editText_ContactPerson1=view.findViewById<EditText>(R.id.editText_ContactPerson1)
        val editText_ContactPhone1=view.findViewById<EditText>(R.id.editText_ContactPhoneNumber1)
        val editText_ContactPerson2=view.findViewById<EditText>(R.id.editText_ContactPerson2)
        val editText_ContactPhone2=view.findViewById<EditText>(R.id.editText_ContactPhoneNumber2)
        val editText_address=view.findViewById<EditText>(R.id.editText_address)
        AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("緊急連絡卡資訊建立")
            .setNegativeButton("取消"){
                dialog,which->

            }
            .setPositiveButton("確定"){
                dialog,which->
                val personName=editText_PersonName.text.toString()
                val health_status=editText_health.text.toString()
                val contactPerson1=editText_ContactPerson1.text.toString()
                val contactPhone1=editText_ContactPhone1.text.toString()
                val contactPerson2=editText_ContactPerson2.text.toString()
                val contactPhone2=editText_ContactPhone2.text.toString()
                val address=editText_address.text.toString()

                val emergencyInfo=UserEmergencyInfo(personName,health_status,contactPerson1,
                    contactPhone1,contactPerson2,contactPhone2,address)
                showProgressDialog()
                if(uid != ""){
                    databaseReference.child(uid).setValue(emergencyInfo).addOnCompleteListener {
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
            }.setOnDismissListener {
                (view.parent as ViewGroup).removeView(view)
            }
            .show()
    }

    private fun showProgressDialog() {
        progressDialog.setMessage("正在儲存中，請稍後...")
        progressDialog.setTitle("儲存資料")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
}