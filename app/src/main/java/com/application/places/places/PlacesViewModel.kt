package com.application.places.places

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.location.Location
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.places.SingleLiveEvent
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.lang.Exception
import kotlin.coroutines.experimental.CoroutineContext

/**
 * ViewModel to retrieve and manage Places
 */
class PlacesViewModel(private val repository: PlacesRepository,
                      private val backgroundCoroutineContext: CoroutineContext,
                      private val postCoroutineContext: CoroutineContext) : ViewModel() {
    val places = ObservableArrayList<Place>()
    var location = MutableLiveData<Location>()
    val placeClicked = SingleLiveEvent<Place>()
    val error = MutableLiveData<String>()
    private val jobs: MutableList<Job> = mutableListOf()

    fun loadData() {
        jobs.add(launch(postCoroutineContext) {
            try {
                val values = withContext(backgroundCoroutineContext) {
                    repository.getAll(location.value?.latitude, location.value?.longitude)
                }
                places.clear()
                places.addAll(values)
            } catch (ex: Exception) {
                error.value = ex.localizedMessage
            }
        })
    }

    fun placeClicked(place: Place) {
        placeClicked.value = place
    }

    override fun onCleared() {
        super.onCleared()
        jobs.forEach {
            it.cancel()
        }
    }
}