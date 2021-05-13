package com.example.corona

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.corona.adapter.PlaceAutoSuggestAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.activity_main3.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val permissions= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERM_FLAG=99

    private lateinit var clusterManager: ClusterManager<MyItem>


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        if(isPermitted()){
            startProcess()
        }else{
            ActivityCompat.requestPermissions(this, permissions, PERM_FLAG)
        }
        val startplace = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        startplace.setAdapter(PlaceAutoSuggestAdapter(this, android.R.layout.simple_list_item_1))
        val endstplace = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        endstplace.setAdapter(PlaceAutoSuggestAdapter(this, android.R.layout.simple_list_item_1))
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
        mMap = googleMap?:return

        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
        setUpClusterer()
        setUpdateLocationListener()


        // 마커 이미지 변환
        var bitmapDrawable:BitmapDrawable
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            bitmapDrawable=getDrawable(R.drawable.picture) as BitmapDrawable
        }else{
            bitmapDrawable=resources.getDrawable(R.drawable.picture) as BitmapDrawable
        }
        var scaledBitmap=Bitmap.createScaledBitmap(bitmapDrawable.bitmap, 50, 50, false)
        var discriptor=BitmapDescriptorFactory.fromBitmap(scaledBitmap)

        var isUp=false
        var translateup:Animation=AnimationUtils.loadAnimation(applicationContext, R.anim.translate_up)

        // 맵 클릭 시 좌표 출력
        mMap.setOnMapClickListener(GoogleMap.OnMapClickListener() {

            var mOption=MarkerOptions()
            mOption.title("마커 좌표")

            var latitude=it.latitude
            var longitude=it.longitude

            mOption.snippet(latitude.toString()+", "+longitude.toString())

            mOption.position(LatLng(latitude, longitude)).icon(discriptor)
            googleMap.addMarker(mOption)

            // 페이지 슬라이딩

            if(isUp){
                page.startAnimation(translateup)
            }
            else{
                page.visibility
                page.startAnimation(translateup)
                isUp=true
            }
        })
    }

    // 내 위치를 가져오는 코드
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback:LocationCallback

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener(){
        val locationRequest=LocationRequest.create()
        locationRequest.run {
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
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
                .zoom(14.0f)
                .build()
        val camera=CameraUpdateFactory.newCameraPosition(cameraOption)

        mMap.addMarker(marker)
        mMap.moveCamera(camera)
    }

    private fun setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(this, mMap)


        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems()
    }

    private fun addItems() {

        // Set some lat/lng coordinates to start with.
        var lat =37.591949
        var lng =127.055867

        var lat1=37.592040761365
        var lng1=127.05692603221914

        var lat2=37.59129264718199
        var lng2=127.05628230204778

        var lat3=37.59076556221925
        var lng3=127.05532743562694

        var lat4=37.591581692189635
        var lng4=127.05416872131849


        val offsetItem = MyItem(lat, lng, "Title ", "Snippet ")
        val offsetItem1 = MyItem(lat1, lng1, "Title ", "Snippet ")
        val offsetItem2 = MyItem(lat2, lng2, "Title ", "Snippet ")
        val offsetItem3 = MyItem(lat3, lng3, "Title ", "Snippet ")
        val offsetItem4 = MyItem(lat4, lng4, "Title ", "Snippet ")

        clusterManager.addItem(offsetItem)
        clusterManager.addItem(offsetItem1)
        clusterManager.addItem(offsetItem2)
        clusterManager.addItem(offsetItem3)
        clusterManager.addItem(offsetItem4)

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