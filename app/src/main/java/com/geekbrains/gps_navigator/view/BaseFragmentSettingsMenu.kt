package com.geekbrains.gps_navigator.view

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.geekbrains.gps_navigator.model.delegates.SharedPreferencesDelegate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

const val LIST_KEY = "LIST_KEY"

abstract class BaseFragmentSettingsMenu<B : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean
    ) -> B
) : Fragment() {
    var listFromSharedPreferences: MutableList<MarkerOptions> by SharedPreferencesDelegate(LIST_KEY)
    lateinit var googleMap: GoogleMap
    var listMarkers: MutableList<MarkerOptions> = mutableListOf()

    private var _binding: B? = null
    protected val binding: B
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return AnimatorDictionary(requireContext()).setAnimator(transit, enter)
    }

    override fun onPause() {
        listFromSharedPreferences = listMarkers
        super.onPause()
    }
}