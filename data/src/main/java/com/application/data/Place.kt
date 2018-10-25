package com.application.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

@Entity(tableName = "Places")
data class Place @JvmOverloads constructor(
        @ColumnInfo(name = "title") var title: String? = "",
        @ColumnInfo(name = "description") var description: String? = "",
        @ColumnInfo(name = "lat") var lat: Double? = null,
        @ColumnInfo(name = "lng") var lng: Double? = null,
        @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(lat)
        parcel.writeValue(lng)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }
}