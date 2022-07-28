package com.example.smart_medical_app.ui.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smart_medical_app.R
import com.example.smart_medical_app.custom_UserCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SettingFragment : Fragment() {
    private lateinit var userCardView:custom_UserCardView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser:FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_setting,null)
        mAuth= FirebaseAuth.getInstance()
        mUser= mAuth.currentUser!!
        userCardView=root.findViewById(R.id.userCardView)
        userCardView.setUserId("UID:"+mUser.uid)
        userCardView.setUserName(mUser.email!!)
        return root
    }

}