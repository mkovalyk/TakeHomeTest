package com.application.places.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.places.R
import com.application.places.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for handling details screen of the place
 */
class DetailsViewModel(private val repository: PlacesRepository,
                       private val backgroundCoroutineContext: CoroutineContext,
                       private val postCoroutineContext: CoroutineContext) : ViewModel() {

    val inEditMode = ObservableBoolean()
    val exit = SingleLiveEvent<Void>()
    val message = SingleLiveEvent<Int>()
    val error = MutableLiveData<String>()
    var place = ObservableField<Place>()
    private var inCreateMode: Boolean = false

    fun initWith(place: Place, inEditMode: Boolean) {
        this.inEditMode.set(inEditMode)
        this.place.set(place)
        this.inCreateMode = inEditMode
    }

    private fun add() {
        CoroutineScope(postCoroutineContext).launch {
            try {
                withContext(backgroundCoroutineContext) {
                    repository.add(place.get()!!)
                }
            } catch (ex: Exception) {
                error.value = ex.localizedMessage
            }
        }
    }

    fun back() {
        if (inCreateMode || !inEditMode.get()) {
            exit.call()
        } else {
            inEditMode.set(false)
        }
    }

    fun enterEditMode() {
        inEditMode.set(true)
    }

    fun confirm() {
        if (place.get()?.title.isNullOrEmpty()) {
            message.value = R.string.error_empty_title
        } else {
            add()
            exit.call()
        }
    }
}