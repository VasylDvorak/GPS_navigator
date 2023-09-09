package com.geekbrains.gps_navigator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.geekbrains.gps_navigator.databinding.ActivityMapsBinding

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : BaseFragmentSettingsMenu<ActivityMapsBinding>(
    ActivityMapsBinding::inflate
) {



    private var listMarkers:MutableList<MarkerOptions> = mutableListOf()
    private var counter=0
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mpa))
        // Add a marker in Sydney and move the camera
        val kerch = LatLng(45.355560707166646, 36.46885790252713)
        googleMap.addMarker(MarkerOptions().position(kerch).title("Керчь"))

        googleMap.isTrafficEnabled = true
        enableLocation()
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMyLocationChangeListener {

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
        googleMap.setOnMapClickListener {latLng->
            var newMarker = MarkerOptions().position( latLng).title("Маркер № "+"${++counter}")
            googleMap.addMarker(newMarker)
            listMarkers.add(newMarker)
        }
    }

    private fun enableLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
            return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}