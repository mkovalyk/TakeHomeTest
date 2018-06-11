package com.application.data.source.remote

import com.google.gson.annotations.SerializedName

/**
 * Entity which is received from the server
 */
class PlacesEntity {
    @SerializedName("locations")
    var locations: List<Location> = listOf()
}

class Location constructor(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("lat")
        var lat: Double? = null,
        @SerializedName("lng")
        var lng: Double? = null)
