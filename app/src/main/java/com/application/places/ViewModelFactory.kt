package com.application.places

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.application.data.PlacesRepository
import com.application.places.place.AddPlaceViewModel
import com.application.places.places.PlacesViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI

/**
 * Factory that provides ViewModels
 */
class ViewModelFactory private constructor(val application: Application, private val placesRepository: PlacesRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(PlacesViewModel::class.java) ->
                        PlacesViewModel(placesRepository, CommonPool, UI)
                    isAssignableFrom(AddPlaceViewModel::class.java) ->
                        AddPlaceViewModel()
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    val factory = CommonFactory.getInstance(application)
                    INSTANCE ?: ViewModelFactory(application, factory.placesRepository)
                            .also { INSTANCE = it }
                }
    }
}
