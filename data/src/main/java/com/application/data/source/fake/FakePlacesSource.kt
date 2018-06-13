package com.application.data.source.fake

import com.application.data.Place
import com.application.data.source.PlacesSource

/**
 * Simple
 */
class FakePlacesSource : PlacesSource {
    private val items = mutableListOf<Place>()
    override suspend fun getAll(): List<Place> {
        return items
    }

    override suspend fun addAll(places: List<Place>) {
        items.addAll(places)
    }

    override suspend fun add(place: Place) {
        items.add(place)
    }

    override suspend fun removeAll() {
        items.clear()
    }

}