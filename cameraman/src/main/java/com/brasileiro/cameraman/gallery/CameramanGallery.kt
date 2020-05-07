package com.brasileiro.cameraman.gallery

import com.brasileiro.cameraman.gallery.model.CameramanAlbum
import com.brasileiro.cameraman.gallery.model.CameramanPicture
import com.brasileiro.cameraman.gallery.view.album.FragmentAlbum
import com.brasileiro.cameraman.gallery.listener.CameramanGalleryListener
import com.brasileiro.cameraman.gallery.view.picture.FragmentGalleryPicture

/**
 * @author Lucas Cota
 * @since 23/08/2019 10:37
 */

class CameramanGallery {

    companion object {
        lateinit var listener: CameramanGalleryListener<*>
    }

    fun build(listener: CameramanGalleryListener<CameramanAlbum>): FragmentAlbum {
        CameramanGallery.listener = listener

        return FragmentAlbum()
    }

    fun build(listener: CameramanGalleryListener<CameramanPicture>): FragmentGalleryPicture {
        CameramanGallery.listener = listener

        return FragmentGalleryPicture()
    }
}
