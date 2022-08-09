package com.example.smart_medical_app.ui.chat


import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_medical_app.MessageAdapter
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
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var btn_createGroup:Button
    private lateinit var btn_add_Group:Button
    private lateinit var btn_closeQRcode:Button
    private lateinit var img_customQRcode:ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageArrayList:ArrayList<MessageList>
    private lateinit var recyclerView_message: RecyclerView
    private lateinit var editText_message:EditText
    private lateinit var btn_sendMessage:Button
    private lateinit var MyDialog:Dialog
    private var groupName:String=""
    private var groupId:Int=-1
    private var uid:String=""
    private lateinit var databaseReference:DatabaseReference
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_group,menu)
        val addGroupMember=menu.findItem(R.id.action_addGroupMember)
        addGroupMember.setOnMenuItemClickListener(object :MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                Log.e("JAMES","addGroupMember")
                buildDialog()
                return false
            }
        })
    }

    private fun buildDialog() {
        MyDialog= Dialog(requireContext())
        MyDialog.setContentView(R.layout.custom_dialog_qrcode)
        btn_closeQRcode=MyDialog.findViewById(R.id.button_closeQRcode)
        img_customQRcode=MyDialog.findViewById(R.id.imageView_custom_qrcode)
        img_customQRcode.setImageBitmap(generateQRCode())
        btn_closeQRcode.isEnabled=true
        btn_closeQRcode.setOnClickListener {
            Log.e("JAMES","closeQRCode")
            MyDialog.dismiss()
        }
        MyDialog.show()
    }

    private fun generateQRCode(): Bitmap {
        val jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("groupName",groupName)
        jsonObject.addProperty("groupId",groupId)
        val text=jsonObject.toString()
        val writer: MultiFormatWriter = MultiFormatWriter()
        try{
            val matrix: BitMatrix =writer.encode(text, BarcodeFormat.QR_CODE,350,350)
            val encoder: BarcodeEncoder = BarcodeEncoder()
            val bitmap:Bitmap=encoder.createBitmap(matrix)
            return bitmap

        }catch (e: WriterException){
            e.printStackTrace()
        }
        return BitmapFactory.decodeResource(requireContext().resources,R.drawable.ic_baseline_error_24)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("JAMES","PositionFragmentOnCreate")
        val root=inflater.inflate(R.layout.fragment_chat,null)
        setHasOptionsMenu(true)
        btn_createGroup=root.findViewById(R.id.button_createGroup)
        btn_add_Group=root.findViewById(R.id.button_addGroup)
        recyclerView_message=root.findViewById(R.id.recyclerView_message)
        editText_message=root.findViewById(R.id.editTextMessage)
        btn_sendMessage=root.findViewById(R.id.button_send)
        mAuth= FirebaseAuth.getInstance()
        uid=mAuth.currentUser?.uid?:""
        messageArrayList= ArrayList()
        messageAdapter= MessageAdapter(requireContext(),messageArrayList)
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_Group")
        val linearLayoutManager= LinearLayoutManager(requireContext())
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        recyclerView_message.layoutManager=linearLayoutManager
        recyclerView_message.visibility=View.INVISIBLE
        editText_message.visibility=View.INVISIBLE
        btn_sendMessage.visibility=View.INVISIBLE
        return root
    }

    override fun onResume() {
        super.onResume()
        btn_add_Group.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_navigation_qrcodeScanner)
        }
        btn_createGroup.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_chat_to_navigation_qrcode)
        }
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageArrayList.clear()
                for(data in snapshot.children){
                    for(item in data.child("member").children){
                        if(uid==item.value.toString()){
                                try{
                                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                                    data.child("groupName").value.toString()
                                    groupName=data.child("groupName").value.toString()
                                    groupId=data.child("id").value.toString().toInt()
                                    btn_createGroup.visibility=View.INVISIBLE
                                    btn_add_Group.visibility=View.INVISIBLE
                                    recyclerView_message.visibility=View.VISIBLE
                                    editText_message.visibility=View.VISIBLE
                                    btn_sendMessage.visibility=View.VISIBLE
                                }catch (e:IllegalStateException){
                                    e.printStackTrace()
                                }
                            Log.e("JAMES",data.child("groupName").value.toString())
                        }
                    }
                    for(item in data.child("message").children){
                        val message:String=item.child("text").value.toString()
                        val uid:String=item.child("uid").value.toString()
                        val time:String=item.child("time").value.toString()
                        val messageListItem:MessageList= MessageList(uid,message,time)
                        messageArrayList.add(messageListItem)
                        Log.e("JAMES",messageArrayList.toString())
                        messageAdapter.notifyDataSetChanged()
                        recyclerView_message.adapter=messageAdapter
                        recyclerView_message.scrollToPosition(messageAdapter.itemCount-1)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        btn_sendMessage.setOnClickListener {
            val message:String=editText_message.text.toString()
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
            val t:String=simpleDateFormat.format(Date())
            val messageList:MessageList=MessageList(uid,message,t)
            messageArrayList.add(messageList)
            databaseReference.child("house$groupId").child("message").setValue(messageArrayList)
            editText_message.text.clear()
        }

    }
}