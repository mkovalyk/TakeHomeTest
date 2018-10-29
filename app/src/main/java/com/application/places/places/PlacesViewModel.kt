package com.application.places.places

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableArrayList
import android.location.Location
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.places.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel to retrieve and manage Places
 */
class PlacesViewModel(private val repository: PlacesRepository,
                      private val backgroundCoroutineContext: CoroutineContext,
                      postCoroutineContext: CoroutineContext) : ViewModel(), CoroutineScope {
    val places = ObservableArrayList<Place>()
    val placeClicked = SingleLiveEvent<Place>()
    val error = MutableLiveData<String>()
    private val jobs: MutableList<Job> = mutableListOf()
    var location = MutableLiveData<Location>()
    override val coroutineContext = postCoroutineContext

    fun loadData() {
        jobs.add(launch(coroutineContext) {
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