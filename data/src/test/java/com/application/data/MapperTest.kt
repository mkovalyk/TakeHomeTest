package com.application.data

import com.application.data.source.PlaceMapper
import com.application.data.source.remote.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest{

    @Test
    fun `Location to Place map`()
    {
        val location = Location(lat = 1.0, lng = 2.3, name = "Test")
        val result = PlaceMapper.mapToPlace(location)

        assertEquals(location.lat, result.lat)
        assertEquals(location.lng, result.lng)
        assertEquals(location.name, result.title)
    }
}