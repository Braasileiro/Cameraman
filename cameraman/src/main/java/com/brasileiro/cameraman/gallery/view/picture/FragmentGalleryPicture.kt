package com.brasileiro.cameraman.gallery.view.picture

import java.io.Serializable

import kotlin.properties.Delegates

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.extension.gallery
import com.brasileiro.cameraman.extension.visible
import com.brasileiro.cameraman.gallery.CameramanGallery
import com.brasileiro.cameraman.gallery.adapter.PictureAdapter
import com.brasileiro.cameraman.gallery.model.CameramanPicture
import com.brasileiro.cameraman.gallery.listener.CameramanGalleryListener
import com.brasileiro.cameraman.gallery.view.imageviewer.ImageViewerActivity

import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * @author Lucas Cota
 * @since 23/08/2019 10:24
 */

@Suppress("UNCHECKED_CAST")
class FragmentGalleryPicture : Fragment() {

    private var root: View by Delegates.notNull()

    private val listener: CameramanGalleryListener<CameramanPicture> by lazy {
        CameramanGallery.listener as CameramanGalleryListener<CameramanPicture>
    }

    private lateinit var items: List<CameramanPicture>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_gallery, container, false)

        return root
    }

    override fun onResume() {
        super.onResume()

        items = listener.onGallerySetupRecycler()

        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = PictureAdapter(context!!, items, ::onItemClick)

        recycler.gallery(adapter)

        if (items.isNullOrEmpty()) txtNoPictures.visible(true) else txtNoPictures.visible(false)
    }

    private fun onItemClick(position: Int) {
        val intent = Intent(activity, ImageViewerActivity::class.java)
            .putExtra(Cameraman.POSITION, position)
            .putExtra(Cameraman.PICTURES, items as Serializable)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        startActivity(intent)
    }
}
