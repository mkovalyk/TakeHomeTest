package com.application.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.application.data.Place

@Dao
interface PlacesDao {
    @Query("SELECT * FROM Places")
    fun getAllPlaces(): List<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places: List<Place>)

    @Query("DELETE FROM Places")
    fun removeAll()
}