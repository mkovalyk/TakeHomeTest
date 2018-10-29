package com.application.places

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.application.data.Place
import com.application.data.PlacesRepository
import com.application.data.source.PlacesSource
import com.application.data.source.fake.FakeLocalSourceValidator
import com.application.data.source.fake.FakePlacesSource
import com.application.places.places.PlacesViewModel
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.Unconfined
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Tests for [PlacesViewModel]
 */
class PlacesViewModeTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val localStorage = mock<PlacesSource>()
    private val remoteStorage = mock<PlacesSource>()
    private val localSourceTracker = FakeLocalSourceValidator(false)
    private val preFilledItems = mutableListOf<Place>()
    private val fakePlacesRepository: PlacesRepository

    init {
        preFilledItems.add(Place("First", "Description", 0.0, 0.4))
        preFilledItems.add(Place("Second", "Description", 0.0, 0.3))
        preFilledItems.add(Place("Third", "Description", 0.0, 0.2))
        preFilledItems.add(Place("Forth", "Description", 0.0, 0.1))

        // create repository
        val localSource = FakePlacesSource()
        runBlocking { localSource.addAll(preFilledItems) }
        fakePlacesRepository = PlacesRepository(localSource, remoteStorage, localSourceTracker)
    }

    @Test
    fun `getAll success`() = runBlocking {
        localSourceTracker.isValid = true
        val placeViewModel = PlacesViewModel(fakePlacesRepository, Unconfined, Unconfined)

        placeViewModel.loadData()

        assertEquals(preFilledItems.size, placeViewModel.places.size)
        assertNull(placeViewModel.error.value)
    }

    @Test
    fun `getAll error`() = runBlocking {
        val placesRepository = PlacesRepository(localStorage, remoteStorage, localSourceTracker)
        val placeViewModel = PlacesViewModel(placesRepository, Unconfined, Unconfined)
        // throw exception
        Mockito.`when`(localStorage.getAll()).thenThrow(IllegalStateException("Transaction is not allowed"))

        placeViewModel.loadData()

        assertEquals(0, placeViewModel.places.size)
        assertNotNull(placeViewModel.error)
    }

    @Test
    fun `placeClicked invoked`() = runBlocking {
        val placesRepository = PlacesRepository(localStorage, remoteStorage, localSourceTracker)
        val placeViewModel = PlacesViewModel(placesRepository, Unconfined, Unconfined)

        val place = Place("Test")
        val spy = spy<Observer<Place>>()
        placeViewModel.placeClicked.observeForever(spy)

        placeViewModel.placeClicked(place)

        verify(spy).onChanged(eq(place))
    }
}
