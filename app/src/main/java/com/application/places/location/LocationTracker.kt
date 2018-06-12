package com.application.places.location

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

/**
 * Class for tracking location
 */
class LocationTracker(private val fusedLocationClient: FusedLocationProviderClient) {
    private var locationRequest = LocationRequest()
    private var successListener: ((location: Location) -> Unit)? = null
    private var failureListener: ((exception: Exception) -> Unit)? = null

    init {
        with(locationRequest)
        {
            interval = java.util.concurrent.TimeUnit.SECONDS.toMillis(10)
            fastestInterval = java.util.concurrent.TimeUnit.SECONDS.toMillis(5)
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null || locationResult.lastLocation == null) {
                failureListener?.invoke(IllegalStateException("Can not receive location"))
            } else {
                successListener?.invoke(locationResult.lastLocation)
            }
        }
    }

    // TODO add lifecycle handling
    fun startTracking(success: (location: Location) -> Unit, failure: (exception: Exception) -> Unit) {
        this.successListener = success
        this.failureListener = failure
        try {
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { it -> success.invoke(it) }
                    .addOnFailureListener { it -> failure.invoke(it) }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (ex: SecurityException) {
            failure.invoke(ex)
        }
    }

    fun stopTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
