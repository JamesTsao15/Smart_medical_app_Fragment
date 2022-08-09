package com.example.smart_medical_app

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MessageAdapter(val context:Context,val messageArrayList: ArrayList<MessageList>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val ITEM_SENT=1
    val ITEM_RECEIVE=2
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_ID")
        if(viewType==ITEM_SENT){
            val view:View=LayoutInflater.from(context).inflate(R.layout.custom_view_send_message,parent,false)
            return SentViewHolder(view)
        }
        else{
            val view:View=LayoutInflater.from(context).inflate(R.layout.custom_view_recieve_message,parent,false)
            return ReceiveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageArrayList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.uid)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage=messageArrayList[position]
        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
            holder.sentMessage.text=currentMessage.text
            holder.sentMessage.autoLinkMask=Linkify.WEB_URLS
            holder.sentMessage.movementMethod=LinkMovementMethod.getInstance()
        }
        else{
            val viewHolder=holder as ReceiveViewHolder
            holder.receiveMessage.text=currentMessage.text
            holder.receiveMessage.autoLinkMask=Linkify.WEB_URLS
            holder.receiveMessage.movementMethod=LinkMovementMethod.getInstance()
            databaseReference.child(currentMessage.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.getValue()!=null){
                        holder.userName.text=snapshot.getValue().toString()
                    }
                    else{
                        holder.userName.text=currentMessage.uid
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }

            }
            )
        }
    }

    override fun getItemCount(): Int {
        return messageArrayList.size
    }
    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.textView_sentMessage)
    }
    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.textView_receiveMessage)
        val userName=itemView.findViewById<TextView>(R.id.textView_userNameMessage)
    }
}