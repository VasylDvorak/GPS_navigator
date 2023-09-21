package com.gps_navigator.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gps_navigator.model.delegates.SharedPreferencesDelegate
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

const val LIST_KEY = "LIST_KEY"

class MapFragmentRepo : IMapFragmentRepo {
    var listFromSharedPreferences: MutableList<MarkerOptions> by SharedPreferencesDelegate(LIST_KEY)
    private var listMarkers: MutableList<MarkerOptions> = mutableListOf()

    override fun getMarkers(): Single<MutableList<MarkerOptions>> {
        if (!listFromSharedPreferences.isNullOrEmpty()) {
            listMarkers = listFromSharedPreferences
        } else {
            listMarkers = mutableListOf()
        }
        val output = listMarkers
        return Single.fromCallable { output }.subscribeOn(Schedulers.io())
    }

    override fun addMarkerOnMap(latLng: LatLng): MarkerOptions {
        var counter = listMarkers.size
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

        val newMarker = MarkerOptions()
            .position(latLng)
            .title("Маркер № " + "${counter}")
            .snippet("Аннотация № " + "${counter}")
            .draggable(true)
        listMarkers.add(newMarker)

        return newMarker
    }

    override fun draggMarker(arg0: Marker, titleMarker: String?) {
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

    override fun saveListMarkers() {
        listFromSharedPreferences = listMarkers
    }

    override fun onCorrectionClick(i: Int, marker: MarkerOptions) {
        listMarkers[i] = marker
    }

    override fun onRemove(i: Int): MutableList<MarkerOptions> {
        listMarkers.removeAt(i)
        return listMarkers
    }

}
