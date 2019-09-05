package com.brasileiro.cameraman.model

import java.io.Serializable

/**
 * @author Lucas Cota
 * @since 23/08/2019 09:59
 */

data class CameramanPicture(
    var path: String,
    var description: String? = null

) : Serializable
