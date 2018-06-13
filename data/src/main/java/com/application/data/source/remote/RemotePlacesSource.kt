package com.application.data.source.remote

import com.application.data.Place
import com.application.data.source.PlaceMapper.mapToPlace
import com.application.data.source.PlacesSource
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * Source that is used for communication with remote server.
 */
class RemotePlacesSource(private val gson: Gson, private val client: OkHttpClient) : PlacesSource {
    override suspend fun getAll(): List<Place> {
        // Create request for remote resource.
        val request = Request.Builder()
                .url(ENDPOINT)
                .build()
        val response = client.newCall(request).execute()

        val places = gson.fromJson(response.body()?.charStream(), PlacesEntity::class.java)
        val result = mutableListOf<Place>()
        places?.locations?.forEach { result.add(mapToPlace(it)) }
        return result
    }

    override suspend fun addAll(places: List<Place>) {
        // Not implemented for now
    }

    override suspend fun add(place: Place) {
        // Not implemented for now
    }

    override suspend fun removeAll() {
        // Not implemented for now
    }

    companion object {
        const val ENDPOINT = "https://s3-ap-southeast-2.amazonaws.com/com-cochlear-sabretooth-takehometest/locations.json"
    }
}
