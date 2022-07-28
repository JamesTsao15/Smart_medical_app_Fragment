package com.example.smart_medical_app.ui.monitor

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smart_medical_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import java.lang.IllegalStateException
import java.lang.RuntimeException

class MonitorFragment : Fragment(),SurfaceHolder.Callback,VlcListener {
    private var uri:String="rtsp://123.195.87.158:8554/cam"
    private var james_uncle_uri:String="rtsp://admin:admin@220.134.71.240:550/h264/main/av_stream"
    private lateinit var vlcVideoLibrary: VlcVideoLibrary
    private lateinit var surfaceView_monitor: SurfaceView
    private lateinit var surfaceView_progressBar: ProgressBar
    private lateinit var bottomNavigationView: BottomNavigationView
    private fun prepareVLCPlayer(surface:Surface){
        Log.e("JAMES","prepareVLCPlayer")
        vlcVideoLibrary= VlcVideoLibrary(requireActivity(),this,surface,
            surfaceView_monitor.width,surfaceView_monitor.height)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("JAMES","MonitorFragment_onCreate")
        val root=inflater.inflate(R.layout.fragment_monitor,null)
        surfaceView_monitor=root.findViewById(R.id.surfaceView_monitor)
        surfaceView_progressBar=root.findViewById(R.id.progressBar_surfaceview)
        surfaceView_progressBar.isIndeterminate=true
        surfaceView_monitor.holder.addCallback(this)
        return root
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val surface=holder.surface
        prepareVLCPlayer(surface)
        vlcVideoLibrary.play(uri)
    }

    override fun onPause() {
        super.onPause()
        Log.e("JAMES","MonitorFragment_onPause")
        try{
            vlcVideoLibrary.stop()
        }catch (e:RuntimeException){
            e.printStackTrace()
        }
    }
    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    override fun onComplete() {
        Toast.makeText(requireContext(),"playing", Toast.LENGTH_SHORT).show()
        surfaceView_progressBar.visibility=View.INVISIBLE
    }

    override fun onError() {
        try {
            Toast.makeText(requireContext(),"Error, make sure your endpoint is correct",Toast.LENGTH_SHORT).show()
            vlcVideoLibrary.stop()
        }
        catch (e:IllegalStateException){
            e.printStackTrace()
        }

    }

}