package com.application.places

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.application.data.Place
import com.application.places.place.AddPlaceViewModel
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [AddPlaceViewModel]
 */
class AddPlaceViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `initWith verify`() {
        val addPlaceViewModel = AddPlaceViewModel()

        val place = Place("Test")
        val inEditMode = true
        addPlaceViewModel.initWith(place, inEditMode, null)

        Assert.assertEquals(place, addPlaceViewModel.place.get())
        Assert.assertEquals(inEditMode, addPlaceViewModel.inEditMode.get())
    }

    @Test
    fun cancelClicked() {
        val addPlaceViewModel = AddPlaceViewModel()
        val spy = spy<Observer<Void>>()
        addPlaceViewModel.cancelClicked.observeForever(spy)

        addPlaceViewModel.cancel()

        verify(spy).onChanged(anyOrNull())
    }

    @Test
    fun saveClicked() {
        val addPlaceViewModel = AddPlaceViewModel()
        val spy = spy<Observer<Void>>()
        addPlaceViewModel.saveClicked.observeForever(spy)

        addPlaceViewModel.save()

        verify(spy).onChanged(anyOrNull())
    }

    @Test
    fun detailsClicked() {
        val addPlaceViewModel = AddPlaceViewModel()
        val spy = spy<Observer<Void>>()
        addPlaceViewModel.detailsClicked.observeForever(spy)

        addPlaceViewModel.details()

        verify(spy).onChanged(anyOrNull())
    }
}