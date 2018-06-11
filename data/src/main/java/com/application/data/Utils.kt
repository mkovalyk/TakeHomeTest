package com.application.data

import android.location.Location

/**
 * A bunch of methods that are used throughout the app
 */
object Utils {
    /**
     *  Evaluates distance between two points. Returns [Float.MAX_VALUE] in case any of the parameter is null
     */
    fun getDistanceBetweenPoints(lat: Double?, lng: Double?, lat1: Double?, lng1: Double?): Float {
        val results = FloatArray(3)
        if (lat == null || lng == null || lat1 == null || lng1 == null) {
            return Float.MAX_VALUE
        }
        Location.distanceBetween(lat, lng, lat1, lng1, results)
        return results[0]
    }

}
