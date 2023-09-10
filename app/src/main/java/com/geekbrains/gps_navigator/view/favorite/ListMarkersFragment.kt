package com.geekbrains.gps_navigator.view.favorite

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.FragmentListMarkersBinding
import com.geekbrains.gps_navigator.view.BaseFragmentSettingsMenu
import com.geekbrains.gps_navigator.view.MapsFragment
import com.geekbrains.gps_navigator.view.TO_MARKER
import com.geekbrains.gps_navigator.view.viewById
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class ListMarkersFragment : BaseFragmentSettingsMenu<FragmentListMarkersBinding>(
    FragmentListMarkersBinding::inflate
) {

    private val historyActivityRecyclerview by viewById<RecyclerView>(R.id.history_activity_recyclerview)

    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter(
            ::onItemClick,
            ::onPlayClick,
            ::onRemove
        )
    }

    private fun onRemove(i: Int, marker: MarkerOptions) {
        listMarkers.removeAt(i)
        setDataToAdapter(listMarkers)
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

    private fun onPlayClick(url: String) {
        TODO()
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


//    private fun iniViewModel() {
//        if (historyActivityRecyclerview.adapter != null) {
//            throw IllegalStateException(getString(R.string.exception_error))
//        }
//
//        model.subscribe().observe(viewLifecycleOwner) { appState ->
//            when (appState) {
//                is AppState.Success -> {
//                    appState.data?.let {
//                        if (it.size != 0) {
//                            renderData(appState)
//                        }
//                    }
//                }
//
//                else -> {}
//            }
//        }
//    }

    private fun initViews() {
        historyActivityRecyclerview.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(
            historyActivityRecyclerview
        )
    }

    companion object {
        fun newInstance() = ListMarkersFragment()
    }
}
