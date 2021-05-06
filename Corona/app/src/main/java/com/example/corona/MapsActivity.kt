package com.example.corona

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val permissions= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERM_FLAG=99
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
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
                .title("현재 위치")
        val cameraOption=CameraPosition.Builder()
                .target(myLocation)
                .zoom(15.0f)
                .build()
        val camera=CameraUpdateFactory.newCameraPosition(cameraOption)

        mMap.addMarker(marker)
        mMap.moveCamera(camera)

        // 마커 이미지 변환
        var bitmapDrawable:BitmapDrawable
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            bitmapDrawable=getDrawable(R.drawable.picture) as BitmapDrawable
        }else{
            bitmapDrawable=resources.getDrawable(R.drawable.picture) as BitmapDrawable
        }
        var scaledBitmap=Bitmap.createScaledBitmap(bitmapDrawable.bitmap, 50, 50, false)
        var discriptor=BitmapDescriptorFactory.fromBitmap(scaledBitmap)




        //코로나 마커 추가
        val corona1=LatLng(37.591949, 127.055867)
        val marker1=MarkerOptions()
                .position(corona1)
                .title("4월22일/청량초등학교")
                .icon(discriptor)
        mMap.addMarker(marker1)

        val corona2=LatLng(37.593485, 127.060046)
        val marker2=MarkerOptions()
                .position(corona2)
                .title("5월1일/이문삼성래미안")
                .icon(discriptor)
        mMap.addMarker(marker2)
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