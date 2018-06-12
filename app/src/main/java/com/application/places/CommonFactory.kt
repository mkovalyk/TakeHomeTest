package com.application.places

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.application.data.PlacesRepository
import com.application.data.source.PlacesSource
import com.application.data.source.local.LocalPlacesSource
import com.application.data.source.local.LocalSourceTracker
import com.application.data.source.local.PlacesDatabase
import com.application.data.source.remote.RemotePlacesSource
import com.application.places.location.LocationTracker
import com.application.places.source.LocalSourceTrackerImpl
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

/**
 * Just a factory that creates and holds all classes that will be used throughout the app.
 * TODO migrate to Dagger in future
 */
@Suppress("MemberVisibilityCanBePrivate")
class CommonFactory private constructor(application: Application) {
    val storageTracker: LocalSourceTracker
    val localSource: PlacesSource
    val remoteSource: PlacesSource
    val placesRepository: PlacesRepository
    val locationTracker: LocationTracker

    init {
        storageTracker = LocalSourceTrackerImpl(application)
        val database = PlacesDatabase.getInstance(application)
        localSource = LocalPlacesSource(database.placeDao())
        remoteSource = RemotePlacesSource(Gson())
        placesRepository = PlacesRepository(localSource, remoteSource, storageTracker)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application as Context)
        locationTracker = LocationTracker(fusedLocationClient)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: CommonFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(CommonFactory::class.java) {
                    val factory = CommonFactory(application)
                    INSTANCE ?: CommonFactory(application)
                            .also { INSTANCE = it }
                }
    }
}