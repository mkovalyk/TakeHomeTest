package com.application.places

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.data.source.PlacesSource
import com.application.data.source.fake.FakeLocalSourceValidator
import com.application.places.detail.DetailsViewModel
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [DetailsViewModel]
 */
class DetailsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val placesRepository = mock<PlacesRepository>()
    private val localStorage = mock<PlacesSource>()
    private val remoteStorage = mock<PlacesSource>()
    private val localSourceTracker = FakeLocalSourceValidator(false)

    @Test
    fun `initWith verify`() {
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)

        val place = Place("Test")
        val inEditMode = true

        detailsViewModel.initWith(place, inEditMode)

        assertEquals(place, detailsViewModel.place.get())
        assertEquals(inEditMode, detailsViewModel.inEditMode.get())
    }

    @Test
    fun `add non-empty title successful`() = runBlocking {
        val placesRepository = PlacesRepository(localStorage, remoteStorage, localSourceTracker)
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)

        val place = Place("Test")
        val inEditMode = true
        detailsViewModel.initWith(place, inEditMode)

        val exit = spy<Observer<Void>>()
        detailsViewModel.exit.observeForever(exit)
        detailsViewModel.confirm()

        verify(localStorage).add(eq(place))
        verify(exit).onChanged(anyOrNull())
    }

    @Test
    fun `add empty_title unsuccessful`() = runBlocking {
        val placesRepository = PlacesRepository(localStorage, remoteStorage, localSourceTracker)
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)

        val place = Place("")
        val inEditMode = true
        detailsViewModel.initWith(place, inEditMode)

        val exit = spy<Observer<Void>>()
        val message = spy<Observer<Int>>()
        with(detailsViewModel) {
            this.exit.observeForever(exit)
            this.message.observeForever(message)
        }

        detailsViewModel.confirm()

        verify(localStorage, never()).add(eq(place))
        verify(exit, never()).onChanged(anyOrNull())
        verify(message).onChanged(any())
    }

    @Test
    fun `enterEditMode successful`() = runBlocking {
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)

        detailsViewModel.enterEditMode()
        assertTrue(detailsViewModel.inEditMode.get())
    }

    @Test
    fun `back inEditMode return_to_prev_state`() {
        val place = Place("")
        val inEditMode = false
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)
                .apply {
                    initWith(place, inEditMode)
                    enterEditMode()
                }

        detailsViewModel.back()

        assertFalse(detailsViewModel.inEditMode.get())
    }

    @Test
    fun `back not_inEditMode exit`() {
        val place = Place("")
        val inEditMode = false
        val exit = spy<Observer<Void>>()
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)
                .apply {
                    initWith(place, inEditMode)
                    this.exit.observeForever(exit)
                }

        detailsViewModel.back()

        assertFalse(detailsViewModel.inEditMode.get())
        verify(exit).onChanged(anyOrNull())
    }

    @Test
    fun `back inCreateMode exit`() {
        val place = Place("")
        val inEditMode = false
        val exit = spy<Observer<Void>>()
        val detailsViewModel = DetailsViewModel(placesRepository, Unconfined, Unconfined)
                .apply {
                    initWith(place, inEditMode)
                    this.exit.observeForever(exit)
                }

        detailsViewModel.back()

        assertFalse(detailsViewModel.inEditMode.get())
        verify(exit).onChanged(anyOrNull())
    }
}