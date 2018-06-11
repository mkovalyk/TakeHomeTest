package com.application.data.source.local

import android.arch.persistence.room.*
import com.application.data.Place

@Dao
interface PlacesDao {
    @Query("SELECT * FROM Places")
    fun getAllPlaces(): List<Place>

    @Query("SELECT * FROM Places WHERE id=:placeId")
    fun getPlaceById(placeId: String): Place

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places: List<Place>)

    @Update
    fun updateTask(place: Place): Int

    @Query("DELETE FROM Places WHERE id=:id")
    fun removeById(id: String): Int

    @Query("DELETE FROM Places")
    fun removeAll()
}