package com.gps_navigator.domain.view_MVP

import com.google.android.gms.maps.model.MarkerOptions
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MapFragmentView : MvpView {
    fun init()
    fun loadMarkers(markers: MutableList<MarkerOptions>)
}
