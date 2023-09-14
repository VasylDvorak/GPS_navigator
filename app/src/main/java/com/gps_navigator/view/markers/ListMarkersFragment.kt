package com.gps_navigator.view.markers

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.FragmentListMarkersBinding
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.gps_navigator.domain.presenters.ListMarkersPresenter
import com.gps_navigator.view.BaseFragment
import com.gps_navigator.view.MapsFragment
import com.gps_navigator.view.TO_MARKER
import com.gps_navigator.view.viewById
import moxy.ktx.moxyPresenter

class ListMarkersFragment : BaseFragment<FragmentListMarkersBinding>(
    FragmentListMarkersBinding::inflate
) {

    val presenter: ListMarkersPresenter by moxyPresenter { ListMarkersPresenter() }
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
        presenter.onCorrectionClick(i, marker)
    }

    private fun onRemove(i: Int, marker: MarkerOptions) {
        setDataToAdapter(presenter.onRemove(i))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun init() {
        listMarkersRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            listMarkersRecyclerview
        )
        presenter.loadMarkers()
    }

    override fun loadMarkers(markers: MutableList<MarkerOptions>) {
        setDataToAdapter(markers)
    }


    private fun setDataToAdapter(data: MutableList<MarkerOptions>) {
        adapter.setData(data)
    }

    override fun onPause() {
        presenter.saveListMarkers()
        super.onPause()
    }

    companion object {
        fun newInstance() = ListMarkersFragment()
    }
}
