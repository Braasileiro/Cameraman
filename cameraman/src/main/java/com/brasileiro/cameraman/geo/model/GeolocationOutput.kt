package com.brasileiro.cameraman.geo.model

import java.io.Serializable

import android.content.Context

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.selector.CoordinateType

/**
 * @author Lucas Cota
 * @since 18/06/2019 14:45
 */

data class GeolocationOutput(
    var latitude: Double,
    var longitude: Double,
    var utm: String,
    var utmX: String,
    var utmY: String
) : Serializable {

    fun toPlotString(context: Context, date: String, type: CoordinateType): String {
        return when (type) {

            CoordinateType.UTM -> "${context.getString(R.string.camera_utm)}$utm\n" +
                    "${context.getString(R.string.camera_utm_x)}$utmX\n" +
                    "${context.getString(R.string.camera_utm_y)}$utmY\n" +
                    "${context.getString(R.string.camera_date)}$date"

            CoordinateType.LAT_LNG -> "${context.getString(R.string.camera_latitude)}$latitude\n" +
                    "${context.getString(R.string.camera_longitude)}$longitude\n" +
                    "${context.getString(R.string.camera_date)}$date"
        }
    }
}
