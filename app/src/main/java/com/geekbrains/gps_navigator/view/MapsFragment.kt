package com.geekbrains.gps_navigator.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson


const val TO_MARKER = "TO_MARKER"

class MapsFragment : BaseFragmentSettingsMenu<FragmentMapsBinding>(
    FragmentMapsBinding::inflate
) {
    private var toMarker: MarkerOptions? = null
    private var counter = 0

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { gMap ->
        googleMap = gMap
        googleMap.clear()
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mpa))
        // Add a marker in Sydney and move the camera
        var kerch = LatLng(45.355560707166646, 36.46885790252713)
        googleMap.addMarker(MarkerOptions().position(kerch).title("Керчь"))

        if (toMarker != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker!!.position, 30f))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kerch, 17f))
        }

        loadMarkers()
        enableLocation()
        googleMap.isTrafficEnabled = true
        googleMap.isMyLocationEnabled = true

        initSpeedMeter()
        addMarkerOnMap()
        draggMarker()
    }

    private fun addMarkerOnMap() {


        googleMap.setOnMapClickListener { latLng ->

            counter = listMarkers.size
            var repeat: Boolean
            do {
                repeat = false
                listMarkers.forEach {
                    if (it.title == ("Маркер № " + "${counter}")) {
                        repeat = true
                    }
                }
                if (repeat) {
                    counter++
                }
            } while (repeat)


            var newMarker = MarkerOptions()
                .position(latLng)
                .title("Маркер № " + "${counter}")
                .snippet("Аннотация № " + "${counter}")
                .icon(
                    BitmapDescriptorFactory
                        .fromBitmap(getBitmapFromVectorDrawable(R.drawable.baseline_diamond_24))
                )
                .draggable(true)
            googleMap.addMarker(newMarker)


            listMarkers.add(
                MarkerOptions()
                    .position(latLng)
                    .title("Маркер № " + "${counter}")
                    .snippet("Аннотация № " + "${counter}")
                    .draggable(true)
            )

        }

    }

    private fun draggMarker() {

        var titleMarker: String? = null

        googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {
                titleMarker = arg0.title + arg0.snippet
            }

            override fun onMarkerDragEnd(arg0: Marker) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
                titleMarker?.let {
                    for (i in 0 until listMarkers.size) {
                        if ((listMarkers[i].title + listMarkers[i].snippet) == it) {
                            listMarkers[i] = MarkerOptions()
                                .position(arg0.position)
                                .title(listMarkers[i].title)
                                .snippet(listMarkers[i].snippet)
                                .draggable(true)
                        }
                    }
                }
            }

            override fun onMarkerDrag(arg0: Marker) {}
        })
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun enableLocation() {
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
        val toMarkerString = arguments?.getString(TO_MARKER, null)
        toMarker = Gson().fromJson(toMarkerString, MarkerOptions::class.java)
        mapFragment?.getMapAsync(callback)
    }

    private fun loadMarkers() {
        listMarkers = mutableListOf()
        if (!listFromSharedPreferences.isNullOrEmpty()) {
            listMarkers = listFromSharedPreferences
            listFromSharedPreferences.forEach {
                it.icon(
                    BitmapDescriptorFactory
                        .fromBitmap(getBitmapFromVectorDrawable(R.drawable.baseline_diamond_24))
                )
                googleMap.addMarker(it)
            }
        }

    }

    private fun initSpeedMeter() {
        googleMap.setOnMyLocationChangeListener {
            binding.speedmeter.text = "${(it.speedAccuracyMetersPerSecond * 3.6).toInt()} \n км/ч"

        }
    }

    companion object {
        fun newInstance(bundle: Bundle): MapsFragment {
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}