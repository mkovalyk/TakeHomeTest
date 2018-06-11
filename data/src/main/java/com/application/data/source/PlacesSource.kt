package com.application.data.source

import com.application.data.Place

interface PlacesSource {
    suspend fun getAll(): List<Place>

    suspend fun addAll(places: List<Place>)
    suspend fun add(place: Place)

    suspend fun update(updated: Place)

    suspend fun remove(id: String)
    suspend fun removeAll()
}