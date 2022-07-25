package com.example.smart_medical_app.ui.personal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ToggleButton
import com.example.smart_medical_app.R

class PersonalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("JAMES","onCreate")
        val root=inflater.inflate(R.layout.fragment_personal,null)
        return root
    }

    override fun onResume() {
        super.onResume()

    }

}