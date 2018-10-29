package com.application.places.places

import androidx.lifecycle.Observer
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.application.places.CommonFactory
import com.application.places.R
import com.application.places.detail.DetailsActivity
import com.application.places.location.LocationTracker
import com.application.places.util.obtainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {
    private val placesViewModel: PlacesViewModel by lazy { obtainViewModel(PlacesViewModel::class.java) }
    private val sectionsPagerAdapter: SectionsPagerAdapter by lazy { SectionsPagerAdapter(supportFragmentManager) }
    private lateinit var locationTracker: LocationTracker
    lateinit var job : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.adapter = sectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        locationTracker = CommonFactory.getInstance(application).locationTracker
        placesViewModel.location.observe(this, Observer {
            placesViewModel.loadData()
        })
        placesViewModel.placeClicked.observe(this, Observer {
            startActivity(DetailsActivity.create(this, it!!, false))
        })
        placesViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onStart() {
        super.onStart()
        locationTracker.startTracking(success = { it ->
            placesViewModel.location.value = it
        }, failure = { _ ->
            Toast.makeText(this, "Location can not be retrieved", Toast.LENGTH_SHORT).show()
        })
        // get for the first time
        placesViewModel.loadData()
    }

    override fun onStop() {
        super.onStop()
        locationTracker.stopTracking()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return MapFragment.newInstance()
                1 -> return PlaceListDialogFragment.newInstance()
            }
            throw IllegalStateException("There is no fragment allowed for this position: $position")
        }

        override fun getCount() = TAB_COUNT
    }

    companion object {
        private const val TAB_COUNT = 2
    }
}