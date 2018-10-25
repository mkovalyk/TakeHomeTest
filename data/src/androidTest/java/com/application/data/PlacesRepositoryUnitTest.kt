package com.application.data

import androidx.test.runner.AndroidJUnit4
import com.application.data.Utils.getDistanceBetweenPoints
import com.application.data.source.PlacesSource
import com.application.data.source.local.LocalSourceValidator
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Test for [PlacesRepository] which uses [android.location.Location] class
 */
@RunWith(AndroidJUnit4::class)
class PlacesRepositoryUnitTest {
    private val localStorage = mock<PlacesSource>()
    private val remoteStorage = mock<PlacesSource>()
    private val localSourceTracker = mock<LocalSourceValidator>()
    private val preFilledItems = mutableListOf<Place>()

    @Before
    fun before() {
        preFilledItems.add(Place("First", "Description", 0.0, 0.4))
        preFilledItems.add(Place("Second", "Description", 0.0, 0.3))
        preFilledItems.add(Place("Third", "Description", 0.0, 0.2))
        preFilledItems.add(Place("Forth", "Description", 0.0, 0.1))
    }

    @Test
    fun testSorting() = runBlocking {
        Mockito.`when`(localSourceTracker.isValid).thenReturn(true)
        Mockito.`when`(localStorage.getAll()).thenReturn(preFilledItems)

        val placesRepository = PlacesRepository(localStorage, remoteStorage, localSourceTracker)
        val result = placesRepository.getAll(0.0, 0.0)

        assertEquals(result.size, preFilledItems.size)

        // ensure result is sorted
        var previous = 0.0f
        result.forEach {
            val distanceBetweenPoints = getDistanceBetweenPoints(it.lat, it.lng, 0.0, 0.0)
            Assert.assertTrue(previous <= distanceBetweenPoints)
            previous = distanceBetweenPoints
        }
    }

}