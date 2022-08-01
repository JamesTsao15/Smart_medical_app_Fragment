package com.example.smart_medical_app.ui.qrcode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smart_medical_app.R
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QrcodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_qrcode,null)
        val writer:MultiFormatWriter= MultiFormatWriter()
        return root
    }
}