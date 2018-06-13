package com.application.data

import com.application.data.source.PlacesSource
import com.application.data.source.fake.FakeLocalSourceValidator
import com.application.data.source.fake.FakePlacesSource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * Tests for [PlacesRepository]
 */
class PlacesRepositoryTest {
    private val remoteStorage = mock<PlacesSource>()
    private val localSourceTracker = FakeLocalSourceValidator(false)
    private val preFilledItems = mutableListOf<Place>()

    init {
        preFilledItems.add(Place("First", "Description", 0.0, 0.4))
        preFilledItems.add(Place("Second", "Description", 0.0, 0.3))
        preFilledItems.add(Place("Third", "Description", 0.0, 0.2))
        preFilledItems.add(Place("Forth", "Description", 0.0, 0.1))
    }

    @Test
    fun `get all local storage not valid successful result`() = runBlocking {
        localSourceTracker.isValid = false
        `when`(remoteStorage.getAll()).thenReturn(preFilledItems)

        val fakePlacesSource = FakePlacesSource()
        val placesRepository = PlacesRepository(fakePlacesSource, remoteStorage, localSourceTracker)
        val result = placesRepository.getAll(null, null)

        assertEquals(result.size, preFilledItems.size)
        assertTrue(localSourceTracker.isValid)
    }

    @Test
    fun `get all local storage not valid unsuccessful result`() = runBlocking {
        localSourceTracker.isValid = false
        `when`(remoteStorage.getAll()).thenThrow(IllegalStateException("Network is not available"))

        val fakePlacesSource = FakePlacesSource()
        val placesRepository = PlacesRepository(fakePlacesSource, remoteStorage, localSourceTracker)
        try {
            placesRepository.getAll(null, null)
            fail("Exception should be thrown")
        } catch (ex: Exception) {

        }
        assertFalse(localSourceTracker.isValid)
    }


    @Test
    fun `get all if local is valid`() = runBlocking {
        localSourceTracker.isValid = false
        `when`(remoteStorage.getAll()).thenReturn(preFilledItems)

        val fakePlacesSource = FakePlacesSource()
        val placesRepository = PlacesRepository(fakePlacesSource, remoteStorage, localSourceTracker)
        val result = placesRepository.getAll(null, null)

        assertEquals(result.size, preFilledItems.size)
        assertEquals(result.size, fakePlacesSource.getAll().size)
    }

    @Test
    fun `add successful`() = runBlocking {
        val fakePlacesSource = FakePlacesSource()
        val placesRepository = PlacesRepository(fakePlacesSource, remoteStorage, localSourceTracker)
        placesRepository.add(preFilledItems[0])

        assertEquals(1, fakePlacesSource.getAll().size)
    }

    @Test
    fun `add unsuccessful`() = runBlocking {
        `when`(remoteStorage.add(any())).thenThrow(IllegalStateException("Network is not available"))

        val fakePlacesSource = FakePlacesSource()
        val placesRepository = PlacesRepository(fakePlacesSource, remoteStorage, localSourceTracker)
        try {
            placesRepository.add(preFilledItems[0])
            fail()
        } catch (ex: Exception) {
            assertEquals(0, fakePlacesSource.getAll().size)
        }
    }
}