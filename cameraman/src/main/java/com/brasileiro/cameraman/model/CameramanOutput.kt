package com.brasileiro.cameraman.model

import java.io.File
import java.io.Serializable

import com.brasileiro.cameraman.geo.model.GeolocationOutput

/**
 * @author Lucas Cota
 * @since 18/06/2019 14:40
 */

data class CameramanOutput(
    var picture: File,
    var date: String?,
    var datePattern: String,
    var description: String?,
    var locationOutput: GeolocationOutput?

) : Serializable {

    override fun toString(): String {
        return "picture: ${picture.path} \n" +
                "date: $date \n" +
                "description: $description \n" +
                "latitude: ${locationOutput?.latitude} \n" +
                "longitude: ${locationOutput?.longitude} \n" +
                "utm: ${locationOutput?.utm} \n" +
                "utmX: ${locationOutput?.utmX} \n" +
                "utmY: ${locationOutput?.utmY}"
    }
}
