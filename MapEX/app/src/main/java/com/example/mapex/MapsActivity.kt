package com.example.mapex

import android.location.Location
import android.location.LocationProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient:GoogleApiClient

    // 위치 정보 얻는 객체
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    //권한 체크 요청 코드 정의
    val requestCodePermission=1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient=
    }

    override fun onStart() {
        mGoogleApiClient.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient.disconnect()
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val hg = LatLng(37.58989200392479, 127.05794481215851)
        mMap.addMarker(MarkerOptions().position(hg).title("회기역"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hg))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

    }

    override fun onConnected(bundle: Bundle?) {
    }

    override fun onConnectionSuspended(i: Int) {
    }

    override fun onConnectionFailed(cr: ConnectionResult) {
    }

    fun mCurrentLocation(v:View){
        mFusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener {  }

    }
}