package com.gps_navigator.domain.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.geekbrains.gps_navigator.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gps_navigator.model.delegates.SharedPreferencesDelegate
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.mp.KoinPlatform.getKoin

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
        return Single.fromCallable { listMarkers }.subscribeOn(Schedulers.io())
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

        listMarkers.add(
            MarkerOptions()
                .position(latLng)
                .title("Маркер № " + "${counter}")
                .snippet("Аннотация № " + "${counter}")
                .draggable(true)
        )

        var newMarker = MarkerOptions()
            .position(latLng)
            .title("Маркер № " + "${counter}")
            .snippet("Аннотация № " + "${counter}")
            .icon(getBitmapFromVectorDrawable(R.drawable.baseline_diamond_24))
            .draggable(true)
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


    override fun getBitmapFromVectorDrawable(drawableId: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(getKoin().get(), drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
