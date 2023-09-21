package com.gps_navigator.domain.presenters


import com.google.android.gms.maps.model.MarkerOptions
import com.gps_navigator.domain.repository.IMapFragmentRepo
import com.gps_navigator.domain.repository.MapFragmentRepo
import com.gps_navigator.domain.view_MVP.MapFragmentView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter

class ListMarkersPresenter : MvpPresenter<MapFragmentView>() {


    private val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()

    private var listMarkersRepo: IMapFragmentRepo = MapFragmentRepo()


    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

    }

    lateinit var callMenuRepo: Single<MutableList<MarkerOptions>>
    fun loadMarkers() {
        callMenuRepo = listMarkersRepo.getMarkers() ?: Single.just(mutableListOf<MarkerOptions>())
        loadMarkersJavaRx()
    }


    fun loadMarkersJavaRx() {
        callMenuRepo
            .observeOn(mainThreadScheduler)
            .subscribe({ markers ->
                if (!markers.isNullOrEmpty()) {
                    viewState.loadMarkers(markers)
                } else {
                    viewState.loadMarkers(mutableListOf())
                }
            }, {
                viewState.loadMarkers(mutableListOf())
            })
    }

    fun onCorrectionClick(i: Int, marker: MarkerOptions) {
        listMarkersRepo.onCorrectionClick(i, marker)
    }

    fun onRemove(i: Int): MutableList<MarkerOptions> = listMarkersRepo.onRemove(i)

    fun saveListMarkers() {
        listMarkersRepo.saveListMarkers()
    }
}
