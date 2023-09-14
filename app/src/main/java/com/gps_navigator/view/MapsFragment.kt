package com.gps_navigator.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.gps_navigator.domain.presenters.MapFragmentPresenter
import moxy.ktx.moxyPresenter


const val TO_MARKER = "TO_MARKER"
val kerch = LatLng(45.355560707166646, 36.46885790252713)

class MapsFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate
) {
    private var toMarker: MarkerOptions? = null
    lateinit var googleMap: GoogleMap

    val presenter: MapFragmentPresenter by moxyPresenter { MapFragmentPresenter() }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { gMap ->
        googleMap = gMap
        googleMap.apply {
            clear()
            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mpa))
            addMarker(
                MarkerOptions().position(kerch).title(getString(R.string.Kerch))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            if (toMarker != null) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker!!.position, 30f))
            } else {
                moveCamera(CameraUpdateFactory.newLatLngZoom(kerch, 17f))
            }
            isMyLocationEnabled = true
            isTrafficEnabled = true
            isBuildingsEnabled = true
        }
        presenter.loadMarkers()
        initSpeedMeter()
        addMarkerOnMap()
        draggMarker()
    }

    private fun addMarkerOnMap() {
        googleMap.setOnMapClickListener { latLng ->
            googleMap.addMarker(presenter.addMarkerOnMap(latLng))
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
                presenter.draggMarker(arg0, titleMarker)
            }

            override fun onMarkerDrag(arg0: Marker) {}
        })
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

    override fun init() {
        enableLocation()
    }

    override fun loadMarkers(markers: MutableList<MarkerOptions>) {
        markers.forEach {
            try {
                googleMap.addMarker(it)
            } catch (e: UninitializedPropertyAccessException) {
            }
        }
    }


    private fun initSpeedMeter() {
        googleMap.setOnMyLocationChangeListener {
            binding.speedMeter.text = "${(it.speedAccuracyMetersPerSecond * 3.6).toInt()} \n км/ч"
        }
    }

    override fun onPause() {
        presenter.saveListMarkers()
        super.onPause()
    }

    companion object {
        fun newInstance(bundle: Bundle): MapsFragment {
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}