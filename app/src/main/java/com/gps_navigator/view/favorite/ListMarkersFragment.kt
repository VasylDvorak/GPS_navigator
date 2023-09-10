package com.gps_navigator.view.favorite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.FragmentListMarkersBinding
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.gps_navigator.view.BaseFragmentSettingsMenu
import com.gps_navigator.view.MapsFragment
import com.gps_navigator.view.TO_MARKER
import com.gps_navigator.view.viewById

class ListMarkersFragment : BaseFragmentSettingsMenu<FragmentListMarkersBinding>(
    FragmentListMarkersBinding::inflate
) {

    private val listMarkersRecyclerview by viewById<RecyclerView>(R.id.list_markers_recyclerview)

    private val adapter: ListMarkersAdapter by lazy {
        ListMarkersAdapter(
            ::onItemClick,
            ::onCorrectionClick,
            ::onRemove
        )
    }

    private fun onItemClick(marker: MarkerOptions) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .replace(R.id.flFragment, MapsFragment.newInstance(Bundle().apply {
                    putString(TO_MARKER, Gson().toJson(marker))
                }))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun onCorrectionClick(i: Int, marker: MarkerOptions) {
        listMarkers[i] = marker
    }

    private fun onRemove(i: Int, marker: MarkerOptions) {
        listMarkers.removeAt(i)
        setDataToAdapter(listMarkers)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (!listFromSharedPreferences.isNullOrEmpty()) {
            listMarkers = listFromSharedPreferences
            setDataToAdapter(listMarkers)
        }
    }

    private fun setDataToAdapter(data: MutableList<MarkerOptions>) {
        adapter.setData(data)
    }

    private fun initViews() {
        listMarkersRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            listMarkersRecyclerview
        )
    }

    companion object {
        fun newInstance() = ListMarkersFragment()
    }
}
