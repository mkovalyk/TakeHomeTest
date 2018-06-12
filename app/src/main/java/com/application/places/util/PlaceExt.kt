package com.application.places.util

import android.content.Context
import android.location.Location
import com.application.data.Place
import com.application.data.Utils
import com.application.places.R
import com.application.places.util.CommonValues.KILOMETER


/**
 * @return [Float.MAX_VALUE] in case either location or place is null
 */
fun Place?.distanceTo(location: Location?): Float {
    if (this == null || location == null) {
        return Float.MAX_VALUE
    }
    return Utils.getDistanceBetweenPoints(this.lat, this.lng, location.latitude, location.longitude)
}


/**
 * Evaluates and localizes distance to place from current location
 */
fun Place.distanceToLocalized(location: Location?, context: Context): String {
    val distanceValue = this.distanceTo(location)
    if (distanceValue < Float.MAX_VALUE) {
        return if (distanceValue > KILOMETER) {
            context.getString(R.string.distance_kilometer, distanceValue.div(KILOMETER))
        } else {
            context.getString(R.string.distance_meter, distanceValue.toInt())
        }
    }
    return context.getString(R.string.distance_too_far)
}

