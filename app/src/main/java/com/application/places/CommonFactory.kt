package com.application.places

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room
import android.content.Context
import com.application.data.PlacesRepository
import com.application.data.source.PlacesSource
import com.application.data.source.local.LocalPlacesSource
import com.application.data.source.local.LocalSourceValidator
import com.application.data.source.local.PlacesDatabase
import com.application.data.source.remote.RemotePlacesSource
import com.application.places.location.LocationTracker
import com.application.places.source.LocalSourceValidatorImpl
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

/**
 * Just a factory that creates and holds all classes that will be used throughout the app.
 * TODO migrate to Dagger in future
 */
@Suppress("MemberVisibilityCanBePrivate")
class CommonFactory private constructor(application: Application) {
    val storageValidator: LocalSourceValidator
    val localSource: PlacesSource
    val remoteSource: PlacesSource
    val placesRepository: PlacesRepository
    val locationTracker: LocationTracker

    init {
        storageValidator = LocalSourceValidatorImpl(application)
        val database = Room.databaseBuilder(application,
                PlacesDatabase::class.java, "Places.db")
                .build()
        localSource = LocalPlacesSource(database.placeDao())
        remoteSource = RemotePlacesSource(Gson(), okhttp3.OkHttpClient())
        placesRepository = PlacesRepository(localSource, remoteSource, storageValidator)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application as Context)
        locationTracker = LocationTracker(fusedLocationClient)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: CommonFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(CommonFactory::class.java) {
                    INSTANCE ?: CommonFactory(application)
                            .also { INSTANCE = it }
                }
    }
}