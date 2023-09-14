package com.gps_navigator.domain.presenters


import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gps_navigator.domain.repository.IMapFragmentRepo
import com.gps_navigator.domain.repository.MapFragmentRepo
import com.gps_navigator.domain.view.MapFragmentView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter


class MapFragmentPresenter : MvpPresenter<MapFragmentView>() {


    private val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()

    private var mapFragmentRepo: IMapFragmentRepo = MapFragmentRepo()


    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        loadMarkers()

    }

    lateinit var callMapFragmentRepo: Single<MutableList<MarkerOptions>>
    fun loadMarkers() {
        callMapFragmentRepo =
            mapFragmentRepo.getMarkers() ?: Single.just(mutableListOf<MarkerOptions>())
        loadMarkersJavaRx()
    }

    fun loadMarkersJavaRx() {
        callMapFragmentRepo
            .observeOn(mainThreadScheduler)
            .subscribe({ markers ->
                if (!markers.isNullOrEmpty()) {
                    viewState.loadMarkers(markers)
                } else {
                }
            }, {
            })


    }

    fun addMarkerOnMap(latLng: LatLng) = mapFragmentRepo.addMarkerOnMap(latLng)

    fun draggMarker(arg0: Marker, titleMarker: String?) {
        mapFragmentRepo.draggMarker(arg0, titleMarker)
    }

    fun saveListMarkers() {
        mapFragmentRepo.saveListMarkers()
    }
}
