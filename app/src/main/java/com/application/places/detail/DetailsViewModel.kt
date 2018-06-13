package com.application.places.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.places.R
import com.application.places.SingleLiveEvent
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

/**
 * ViewModel for handling details screen of the place
 */
class DetailsViewModel(private val repository: PlacesRepository,
                       private val backgroundCoroutineContext: CoroutineContext,
                       private val postCoroutineContext: CoroutineContext) : ViewModel() {

    val inEditMode = ObservableBoolean()
    val exit = SingleLiveEvent<Void>()
    val message = SingleLiveEvent<Int>()
    var place = ObservableField<Place>()
    val error = MutableLiveData<String>()
    private var inCreateMode: Boolean = false

    fun initWith(place: Place, inEditMode: Boolean) {
        this.inEditMode.set(inEditMode)
        this.place.set(place)
        this.inCreateMode = inEditMode
    }

    private fun add() {
        launch(postCoroutineContext) {
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
        inEditMode.set(!inEditMode.get())
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