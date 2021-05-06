package com.example.myview

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    fun startProcess(){
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val LATLNG=LatLng(37.566418, 126.977943)
        val markerOptions=MarkerOptions()
                .position(LATLNG)
                .title("4월22일/서울 시청")

        mMap.addMarker(markerOptions)
    }

    val permissions= arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val PERM_LOCATION=99

    fun checkPermission(){
        var permitted_all=true
        for(permission in permissions){
            val result= ContextCompat.checkSelfPermission(this, permission)
            if(result!= PackageManager.PERMISSION_GRANTED){
                permitted_all=false
                requestPermission()
                break
            }
        }
        if(permitted_all){
            startProcess()
        }
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions, PERM_LOCATION)
    }

    fun confirmAgain(){
        AlertDialog.Builder(this).setTitle("권한 승인 확인")
            .setMessage("위치 관련 권한을 모두 승인하셔야 앱을 사용할 수 있습니다. 권한 승인을 다시하시겠습니까?")
            .setPositiveButton("네", {_,_->
                requestPermission()
            })
            .setNegativeButton("아니요", {_, _->
                finish()
            })
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        when(requestCode){
            99->{
                var granted_all=true
                for(result in grantResults){
                    if(result!= PackageManager.PERMISSION_GRANTED){
                        granted_all=false
                        break
                    }
                }
                if(granted_all){
                    startProcess()
                }else{
                    confirmAgain()
                }
            }
        }
    }
}