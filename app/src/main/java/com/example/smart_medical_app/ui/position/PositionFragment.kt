package com.example.smart_medical_app.ui.position

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.smart_medical_app.R
import com.google.android.gms.location.*
import java.util.*

class PositionFragment : Fragment() {
    private var PERMISION_ID=100
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tv_showLocation:TextView
    private lateinit var btn_getLocation:Button
    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)==
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISION_ID)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==PERMISION_ID){
            if(grantResults.isNotEmpty() && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                Log.e("JAMES","You Have the Permission")
            }
        }
    }
    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager=requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    task->
                        var location:Location?=task.result
                        if(location==null){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                newLocationData()
                            }
                        }
                        else{
                            Log.e("JAMES","Your Location:"+location.longitude+","+location.latitude)
                            tv_showLocation.text="Your Current Location is :\n" +
                                    "Long:${location.longitude},Lat:${location.latitude}\n"+
                                    "${getAddressName(location.latitude, location.longitude)}"
                        }
                }
            }
            else{
                Toast.makeText(requireContext(),"請打開裝置位置(GPS)",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            requestPermission()
        }
    }

    private fun getAddressName(lat: Double, long: Double): String {
        var addressName: String = ""
        var countryName = ""
        val geoCoder = Geocoder(activity, Locale.getDefault())
        val adress = geoCoder.getFromLocation(lat, long, 1)
        Log.e("JAMES",adress.toString())
        addressName = adress.get(0).getAddressLine(0)

        Log.e("JAMES", "Your Address: " + addressName)
        return addressName
    }
    @SuppressLint("MissingPermission")
    fun newLocationData() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 0
            fastestInterval = 0
            numUpdates=1
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback=object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                var lastLocation: Location = locationResult.lastLocation!!
                Log.e("JAMES","Your last location:"+lastLocation.longitude.toString())
                tv_showLocation.text="Your Last Location is :\n" +
                        "Long:${lastLocation.longitude},Lat:${lastLocation.latitude}\n"+
                        "${getAddressName(lastLocation.latitude,lastLocation.longitude)}"
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root=inflater.inflate(R.layout.fragment_position,null)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())
        tv_showLocation=root.findViewById(R.id.textView_location)
        btn_getLocation=root.findViewById(R.id.button_getLocation)
        getAddressName(25.214,121.6323)
        return root
    }

    override fun onResume() {
        super.onResume()
        btn_getLocation.setOnClickListener {
            getLastLocation()
        }
    }
}