package com.geekbrains.gps_navigator

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.geekbrains.gps_navigator.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var listMarkers:MutableList<MarkerOptions> = mutableListOf()
    private var counter=0
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mpa))
        // Add a marker in Sydney and move the camera
        val kerch = LatLng(45.355560707166646, 36.46885790252713)
        mMap.addMarker(MarkerOptions().position(kerch).title("Керчь"))

        mMap.isTrafficEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )

            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationChangeListener {

            binding.speedmeter.text = "${(it.speedAccuracyMetersPerSecond * 3.6).toInt()} \n км/ч"
                //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
        }
        binding.cardView.setOnClickListener{
            binding.mainMap.visibility=View.GONE
            binding.settingcon.visibility=View.VISIBLE
            binding.settingcon.setOnClickListener {
                binding.settingcon.visibility=View.GONE
                binding.mainMap.visibility=View.VISIBLE
            }
        }
//        mMap.setOnMarkerClickListener {marker ->
//            binding.cdClose.visibility = View.VISIBLE
//            binding.remMarkerImage.setOnClickListener {
//                marker.remove()
//                binding.cdClose.visibility = View.GONE
//            }
//            true
//    }
        mMap.setOnMapClickListener {latLng->
            val kerch = latLng
            var newMarker = MarkerOptions().position(kerch).title("Маркер № "+"${++counter}")
            mMap.addMarker(newMarker)
            listMarkers.add(newMarker)
        }

    }
}