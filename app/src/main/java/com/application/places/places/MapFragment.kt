package com.application.places.places

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.application.data.Place
import com.application.places.R
import com.application.places.detail.DetailsActivity
import com.application.places.place.AddPlaceDialogFragment
import com.application.places.util.obtainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Fragment that displays map with places
 * TODO Add databinding..
 */
class MapFragment : SupportMapFragment(), OnMapReadyCallback, AddPlaceDialogFragment.AddPlaceListener {
    private val markers = mutableMapOf<Marker, Place>()
    private val model: PlacesViewModel by lazy { (activity as AppCompatActivity).obtainViewModel(PlacesViewModel::class.java) }

    private val placesChangeListener = object : ObservableList.OnListChangedCallback<ObservableArrayList<Place>>() {
        override fun onChanged(sender: ObservableArrayList<Place>?) {

        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<Place>?, positionStart: Int, itemCount: Int) {
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<Place>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<Place>?, positionStart: Int, itemCount: Int) {
            if (sender != null) {
                // places can only be added
                placesChanged(sender)
            }
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<Place>?, positionStart: Int, itemCount: Int) {
        }
    }
    private var marker: Marker? = null
    private var map: GoogleMap? = null
    private var shouldChangeCamera = true

    //region AddPlaceDialogFragment
    override fun onCancel() {
        marker?.remove()
    }

    override fun onSaveClicked(place: Place) {
        marker?.remove()
        startActivity(DetailsActivity.create(context!!, place, true))
    }

    override fun onDetailsClicked(place: Place) {
        startActivity(DetailsActivity.create(context!!, place, false))
    }
    //endregion AddPlaceDialogFragment

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        placesChanged(model.places)
        val permissionState = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSIONS)
        }
        googleMap.setOnMapLongClickListener {
            marker?.remove()
            marker = googleMap.addMarker(MarkerOptions().position(it))
            val place = Place(lat = it.latitude, lng = it.longitude, title = getString(R.string.default_placeholder_new_location))
            showBottomDialog(place, true)
        }
        googleMap.setOnMarkerClickListener { clickedMarker ->
            val place = markers[clickedMarker]
            place?.apply { showBottomDialog(place, false) }
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val permissionState = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionState == PackageManager.PERMISSION_GRANTED) {
                map?.isMyLocationEnabled = true
            }
        }
    }

    private fun showBottomDialog(place: Place, edit: Boolean) {
        val dialog = AddPlaceDialogFragment.newInstance(place, edit, model.location.value)
        dialog.setTargetFragment(this, REQUEST_CODE_ADD_PLACE_FRAGMENT)
        dialog.show(activity!!.supportFragmentManager, AddPlaceDialogFragment.TAG)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        getMapAsync(this)
        model.places.addOnListChangedCallback(placesChangeListener)
    }

    private fun placesChanged(places: List<Place>) {
        markers.forEach {
            it.key.remove()
        }
        markers.clear()
        map?.apply {
            val latLngBoundsBuilder = LatLngBounds.builder()
            places.forEach { marker -> addMarker(this, marker, latLngBoundsBuilder) }
            if (shouldChangeCamera && places.isNotEmpty()) {
                moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), CAMERA_PADDING))
                shouldChangeCamera = false
            }
        }
    }

    private fun addMarker(map: GoogleMap, place: Place, latlngBoundsBuilder: LatLngBounds.Builder) {
        val location = LatLng(place.lat!!, place.lng!!)
        latlngBoundsBuilder.include(location)
        val marker = map.addMarker(MarkerOptions().position(location).title(place.title))
        markers[marker] = place
    }

    override fun onDestroy() {
        super.onDestroy()
        model.places.removeOnListChangedCallback(placesChangeListener)
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1
        private const val REQUEST_CODE_ADD_PLACE_FRAGMENT = 2
        private const val CAMERA_PADDING = 100
        fun newInstance(): MapFragment = MapFragment()
    }
}