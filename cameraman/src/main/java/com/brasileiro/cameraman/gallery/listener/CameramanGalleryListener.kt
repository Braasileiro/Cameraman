package com.brasileiro.cameraman.gallery.listener

import java.io.Serializable

/**
 * @author Lucas Cota
 * @since 13/01/2020 15:01
 */

interface CameramanGalleryListener<T> : Serializable {

    fun onGallerySetupRecycler(): List<T>
}
