package com.application.data

import com.application.data.Utils.getDistanceBetweenPoints
import com.application.data.source.PlacesSource
import com.application.data.source.local.LocalSourceTracker

/**
 * Repository to work with Places
 */
class PlacesRepository(private val localSource: PlacesSource,
                       private val remoteSource: PlacesSource,
                       private val storageTracker: LocalSourceTracker) {

    suspend fun getAll(lat: Double?, lng: Double?): List<Place> {
        return if (storageTracker.isValid) {
            getFromLocalSortedByDistance(lat, lng)
        } else {
            val places = remoteSource.getAll()
            localSource.removeAll()
            localSource.addAll(places)
            storageTracker.isValid = true
            getFromLocalSortedByDistance(lat, lng)
        }
    }

    private suspend fun getFromLocalSortedByDistance(lat: Double?, lng: Double?) =
    // In future sorting should be moved to DB.
            localSource.getAll().sortedBy { it -> getDistanceBetweenPoints(it.lat, it.lng, lat, lng) }

    suspend fun add(place: Place) {
        remoteSource.add(place)
        localSource.add(place)
    }
}