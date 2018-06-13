package com.application.data.source.local

import com.application.data.Place
import com.application.data.source.PlacesSource

/**
 * Source of the places which stores locally
 */
open class LocalPlacesSource(private val placesDao: PlacesDao) : PlacesSource {
    override suspend fun getAll(): List<Place> {
        return placesDao.getAllPlaces()
    }

    override suspend fun addAll(places: List<Place>) {
        placesDao.insertPlaces(places)
    }

    override suspend fun add(place: Place) {
        placesDao.insertPlace(place)
    }

    override suspend fun removeAll() {
        placesDao.removeAll()
    }

}