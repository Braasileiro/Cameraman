package com.brasileiro.cameraman.gallery.model

import java.io.Serializable

/**
 * @author Lucas Cota
 * @since 13/01/2020 12:50
 */

data class CameramanAlbum(
    var title: String?,
    var pictures: List<CameramanPicture>

) : Serializable
