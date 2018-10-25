package com.application.places.places

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.application.data.Place
import com.application.places.databinding.PlaceListItemBinding
import com.application.places.util.distanceToLocalized

/**
 * Adapter for places.
 */
class PlaceAdapter internal constructor(val placesViewModel: PlacesViewModel) :
        RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    private val places: MutableList<Place> = mutableListOf()

    fun setPlaces(updatedPlace: List<Place>?) {
        if (updatedPlace != null) {
            places.clear()
            places.addAll(updatedPlace)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val placeBinding = PlaceListItemBinding.inflate(layoutInflater, parent, false)
        return PlaceViewHolder(placeBinding)
    }

    override fun getItemCount() = places.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    inner class PlaceViewHolder internal constructor(private val binding: PlaceListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            with(binding) {
                this.place = place
                listener = object : PlaceInteractionListener {
                    override fun onPlaceClicked(place: Place) {
                        placesViewModel.placeClicked(place)
                    }
                }
                distanceTo = place.distanceToLocalized(placesViewModel.location.value, binding.root.context)
                executePendingBindings()
            }
        }
    }
}
