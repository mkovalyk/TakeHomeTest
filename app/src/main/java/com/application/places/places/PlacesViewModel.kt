package com.application.places.places

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableArrayList
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
    val placeClicked = SingleLiveEvent<Place>()
    val error = MutableLiveData<String>()
    private val jobs: MutableList<Job> = mutableListOf()
    var location = MutableLiveData<Location>()

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
        jobs.filter { it.isActive }
                .forEach {
                    it.cancel()
                }
        jobs.clear()
    }
}