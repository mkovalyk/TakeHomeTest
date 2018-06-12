package com.application.places.places

import com.application.data.Place

/**
 * Listener used with data binding for processing actions from user
 */
interface PlaceInteractionListener {
    fun onPlaceClicked(place: Place)
}