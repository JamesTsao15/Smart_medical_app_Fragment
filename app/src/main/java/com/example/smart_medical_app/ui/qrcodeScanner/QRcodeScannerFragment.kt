package com.example.smart_medical_app.ui.qrcodeScanner

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.smart_medical_app.R


class QRcodeScannerFragment : Fragment() {
    private val CAMERA_REQUEST_CODE=101
    private lateinit var codeScanner: CodeScanner
    private lateinit var scanner_view:CodeScannerView
    private lateinit var tv_scanResult:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root=inflater.inflate(R.layout.fragment_q_rcode_scanner,null)
        scanner_view=root.findViewById(R.id.scanner_view)
        tv_scanResult=root.findViewById(R.id.textView_ScanResult)
        setupPermission()
        codeScanner()
        return root
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun codeScanner(){
        codeScanner=CodeScanner(requireContext(),scanner_view)
        codeScanner.apply {
            camera=CodeScanner.CAMERA_BACK
            formats=CodeScanner.ALL_FORMATS
            autoFocusMode=AutoFocusMode.SAFE
            scanMode=ScanMode.CONTINUOUS
            isAutoFocusEnabled=true
            isFlashEnabled=false
            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    tv_scanResult.text=it.text
                }
            }
            errorCallback= ErrorCallback {
                requireActivity().runOnUiThread{
                    Log.e("JAMES","Camera initialization error:${it.message}")
                }
            }
        }
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    private fun setupPermission(){
        val permission=ContextCompat.checkSelfPermission(requireContext(),
        android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty() || grantResults[0] !=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(),"你需要開啟相機權限才得以使用此功能",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(),"開啟相機權限成功",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}