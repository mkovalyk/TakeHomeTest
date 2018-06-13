package com.application.data

import com.application.data.Utils.getDistanceBetweenPoints
import com.application.data.source.PlacesSource
import com.application.data.source.local.LocalSourceValidator

/**
 * Repository to work with Places
 */
open class PlacesRepository(private val localSource: PlacesSource,
                            private val remoteSource: PlacesSource,
                            private val storageValidator: LocalSourceValidator) {

    suspend fun getAll(lat: Double?, lng: Double?): List<Place> {
        return if (storageValidator.isValid) {
            getFromLocalSortedByDistance(lat, lng)
        } else {
            val places = remoteSource.getAll()
            localSource.removeAll()
            localSource.addAll(places)
            storageValidator.isValid = true
            getFromLocalSortedByDistance(lat, lng)
        }
    }

    // In future sorting should be moved to DB.
    private suspend fun getFromLocalSortedByDistance(lat: Double?, lng: Double?) =
            localSource.getAll().sortedBy { it -> getDistanceBetweenPoints(it.lat, it.lng, lat, lng) }

    suspend fun add(place: Place) {
        remoteSource.add(place)
        localSource.add(place)
    }
}