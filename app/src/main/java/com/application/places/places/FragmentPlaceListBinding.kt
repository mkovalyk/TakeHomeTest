package com.application.places.places

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.data.Place

/**
 *  Contains [BindingAdapter]s for the [Place] list.
 */
object FragmentPlaceListBinding {
    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(list: RecyclerView, newPlaces: MutableList<Place>?) {
        with(list.adapter as PlaceAdapter) {
            setPlaces(newPlaces)
        }
    }
}