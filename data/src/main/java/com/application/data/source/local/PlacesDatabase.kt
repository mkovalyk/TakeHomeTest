package com.application.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.data.Place

@Database(entities = [(Place::class)], version = 1)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun placeDao(): PlacesDao
}