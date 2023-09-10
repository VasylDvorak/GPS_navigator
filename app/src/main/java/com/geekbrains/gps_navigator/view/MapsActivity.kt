package com.geekbrains.gps_navigator.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.geekbrains.gps_navigator.R
import com.geekbrains.gps_navigator.databinding.ActivityMapsBinding
import com.geekbrains.gps_navigator.view.favorite.ListMarkersFragment

class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val listMarkersFragment = ListMarkersFragment()
        val mapsFragment = MapsFragment()
        setCurrentFragment(mapsFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mapSwitch -> setCurrentFragment(mapsFragment)
                R.id.listMarkers -> setCurrentFragment(listMarkersFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack("")
            commit()
        }
}