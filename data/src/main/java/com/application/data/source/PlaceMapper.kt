package com.application.data.source

import com.application.data.Place
import com.application.data.source.remote.Location

object PlaceMapper {
    fun mapToPlace(location: Location): Place {
        return Place(title = location.name, lat = location.lat, lng = location.lng)
    }
}
