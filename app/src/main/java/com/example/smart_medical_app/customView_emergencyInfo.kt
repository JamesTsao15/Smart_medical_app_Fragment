package com.example.smart_medical_app

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class customView_emergencyInfo @JvmOverloads constructor(context:Context, attrs:AttributeSet?, defStyleAttr:Int=0)
    :LinearLayout(context, attrs, defStyleAttr){
        val view= View.inflate(context,R.layout.custom_view_emergency_info,this)
        private lateinit var tv_PersonName:TextView
        private lateinit var tv_HelthStatus:TextView
        private lateinit var tv_contactUser1:TextView
        private lateinit var tv_contactPhone1:TextView
        private lateinit var tv_contactUser2:TextView
        private lateinit var tv_contactPhone2:TextView
        private lateinit var tv_address:TextView
        init {
            if(attrs!=null){
                val attributes=context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomEmergencyInfo,0,0
                )
                tv_PersonName=view.findViewById(R.id.customView_tv_PersonName)
                tv_HelthStatus=view.findViewById(R.id.customView_tv_health)
                tv_contactUser1=view.findViewById(R.id.customView_tv_ContactPerson1)
                tv_contactPhone1=view.findViewById(R.id.customView_tv_ContactPhoneNumber1)
                tv_contactUser2=view.findViewById(R.id.customView_tv_ContactPerson2)
                tv_contactPhone2=view.findViewById(R.id.customView_tv_ContactPhoneNumber2)
                tv_address=view.findViewById(R.id.customView_tv_address)
            }
        }
        fun setPersonName(text:String){
            tv_PersonName.text=text
        }
        fun setHealthStatus(text:String){
            tv_HelthStatus.text=text
        }
        fun setContactUser1(text: String){
            tv_contactUser1.text=text
        }
        fun setContactPhone1(text: String){
            tv_contactPhone1.text=text
        }
        fun setContactUser2(text: String){
            tv_contactUser2.text=text
        }
        fun setContactPhone2(text:String){
            tv_contactPhone2.text=text
        }
        fun setAddress(text: String){
            tv_address.text=text
        }


}