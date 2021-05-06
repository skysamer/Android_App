package com.example.goomap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BlankFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val permissions= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERM_FLAG=99
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        if(isPermitted()){
            startProcess()
        }else{
            ActivityCompat.requestPermissions(this, permissions, PERM_FLAG)
        }
    }

    fun isPermitted():Boolean{
        for(perm in permissions){
            if(ContextCompat.checkSelfPermission(this, perm)!= PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    fun startProcess(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)

        setUpdateLocationListener()
    }

    // 내 위치를 가져오는 코드
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback:LocationCallback

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener(){
        val locationRequest=LocationRequest.create()
        locationRequest.run {
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
            interval=1000
        }

        locationCallback=object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for((i, location) in it.locations.withIndex()){
                        Log.d("로케이션", "$i ${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }


        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLastLocation(location: Location){
        val myLocation=LatLng(location.latitude, location.longitude)
        val marker=MarkerOptions()
            .position(myLocation)
            .title("I am here!")
        val cameraOption=CameraPosition.Builder()
            .target(myLocation)
            .zoom(15.0f)
            .build()
        val camera=CameraUpdateFactory.newCameraPosition(cameraOption)

        mMap.addMarker(marker)
        mMap.moveCamera(camera)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERM_FLAG->{
                var check=false
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        check=false
                        break
                    }
                }
                if(check){
                    startProcess()
                }else{
                    Toast.makeText(this, "권한을 승인해야지만 앱을 사용할 수 있습니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}