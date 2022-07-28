package com.example.smart_medical_app

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class custom_UserCardView @JvmOverloads constructor(context:Context,attrs:AttributeSet?,defStyleAttr:Int=0)
    :ConstraintLayout(context,attrs, defStyleAttr) {
        val view= View.inflate(context,R.layout.custom_user_view_layout,this)
        private lateinit var tv_UserCardName:TextView
        private lateinit var tv_UserCardId:TextView
        private lateinit var img_profile_image:ImageView
    init {
        if(attrs!=null){
            val attributes=context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomUserCardView,0,0
            )
            tv_UserCardName=view.findViewById<TextView>(R.id.tv_UserCardName)
            tv_UserCardName.text=attributes.getString(R.styleable.CustomUserCardView_UserName)
            tv_UserCardId=view.findViewById<TextView>(R.id.tv_UserId)
            tv_UserCardId.text=attributes.getString(R.styleable.CustomUserCardView_UserId)
            img_profile_image=view.findViewById<ImageView>(R.id.profile_image)
            img_profile_image.setImageResource(attributes.getResourceId(R.styleable.CustomUserCardView_UserImage,R.drawable.ic_baseline_person_24))
        }
    }
    open fun setUserName(text:String){
        tv_UserCardName.text=text
    }
    open fun setUserId(id:String){
        tv_UserCardId.text=id
    }
    open fun setUserImage(resource:Int){
        img_profile_image.setImageResource(resource)
    }
}