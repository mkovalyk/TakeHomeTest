package com.application.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.application.data.Place

@Database(entities = [(Place::class)], version = 1)
abstract class PlacesDatabase : RoomDatabase() {

    abstract fun placeDao(): PlacesDao

    companion object {
        private var INSTANCE: PlacesDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): PlacesDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            PlacesDatabase::class.java, "Places.db")
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}