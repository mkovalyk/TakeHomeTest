package com.application.places.place

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.location.Location
import com.application.data.Place
import com.application.places.SingleLiveEvent

/**
 * ViewModel for binding with dialog for adding/viewing place
 */
class AddPlaceViewModel : ViewModel() {
    val location = ObservableField<Location>()
    val place = ObservableField<Place>()
    val inEditMode = ObservableBoolean()
    val cancelClicked = SingleLiveEvent<Void>()
    val saveClicked = SingleLiveEvent<Void>()
    val detailsClicked = SingleLiveEvent<Void>()

    fun initWith(place: Place, inEditMode:Boolean, location:Location?)
    {
        this.place.set(place)
        this.inEditMode.set(inEditMode)
        this.location.set(location)
    }
    fun cancel(){
        cancelClicked.call()
    }

    fun save() {
        saveClicked.call()
    }

    fun details() {
        detailsClicked.call()
    }
}
