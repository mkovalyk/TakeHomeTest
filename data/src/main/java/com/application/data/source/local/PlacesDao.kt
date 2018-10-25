package com.application.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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