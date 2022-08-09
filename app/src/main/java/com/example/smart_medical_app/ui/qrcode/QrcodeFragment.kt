package com.example.smart_medical_app.ui.qrcode

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.smart_medical_app.GroupInfo
import com.example.smart_medical_app.MessageList
import com.example.smart_medical_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QrcodeFragment : Fragment() {
    private lateinit var imageview_qrcode:ImageView
    private lateinit var mAuth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btn_generateQRcode:Button
    private lateinit var editText_groupName:EditText
    private var group_count:Int=0
    private var uid:String=""
    private lateinit var tv_group_qrcode:TextView
    private lateinit var memberArrayList: ArrayList<String>
    private lateinit var messageArrayList: ArrayList<MessageList>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val text="house1"
        val root=inflater.inflate(R.layout.fragment_qrcode,null)
        imageview_qrcode=root.findViewById(R.id.imageView_qrcode)
        tv_group_qrcode=root.findViewById(R.id.textView_groupQRcode)
        btn_generateQRcode=root.findViewById(R.id.button_generateQRcode)
        editText_groupName=root.findViewById(R.id.editTextGroupName)
        messageArrayList= ArrayList()
        memberArrayList= ArrayList()
        mAuth= FirebaseAuth.getInstance()
        uid=mAuth.currentUser?.uid ?:""
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_Group")
        imageview_qrcode.visibility=View.INVISIBLE
        tv_group_qrcode.visibility=View.INVISIBLE
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                group_count=snapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("JAMES",error.toString())
            }

        })
        return root
    }

    override fun onResume() {
        super.onResume()
        btn_generateQRcode.setOnClickListener {
            val manger:InputMethodManager=requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manger.hideSoftInputFromWindow(requireView().windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            val groupName:String=editText_groupName.text.toString()
            val jsonObject:JsonObject= JsonObject()
            jsonObject.addProperty("groupName",groupName)
            jsonObject.addProperty("groupId",group_count)
            memberArrayList.clear()
            memberArrayList.add(uid)
            messageArrayList.clear()
            val simpleDateFormat:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
            val t:String=simpleDateFormat.format(Date())
            messageArrayList.add(MessageList(uid,"創建群組成功",t))
            databaseReference.child("house"+group_count.toString())
                .setValue(GroupInfo(group_count,groupName,memberArrayList,messageArrayList))
            generateQRCode(jsonObject.toString())

        }
    }
    private fun generateQRCode(text:String) {
        imageview_qrcode.visibility=View.VISIBLE
        tv_group_qrcode.visibility=View.VISIBLE
        val writer:MultiFormatWriter= MultiFormatWriter()
        try{
            val matrix:BitMatrix=writer.encode(text,BarcodeFormat.QR_CODE,350,350)
            val encoder:BarcodeEncoder= BarcodeEncoder()
            val bitmap:Bitmap=encoder.createBitmap(matrix)
            imageview_qrcode.setImageBitmap(bitmap)

        }catch (e:WriterException){
            e.printStackTrace()
        }
    }
}