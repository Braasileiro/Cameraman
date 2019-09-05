package com.brasileiro.cameraman.gallery

import java.io.Serializable

import kotlin.properties.Delegates

import android.os.Build
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

import androidx.fragment.app.Fragment

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.extension.gallery
import com.brasileiro.cameraman.extension.visible
import com.brasileiro.cameraman.model.CameramanPicture
import com.brasileiro.cameraman.gallery.adapter.GalleryAdapter
import com.brasileiro.cameraman.listener.CameramanGalleryListener

import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * @author Lucas Cota
 * @since 23/08/2019 10:24
 */

@Suppress("UNCHECKED_CAST")
class FragmentGallery : Fragment() {

    private var root: View by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_gallery, container, false)

        return root
    }

    private lateinit var pictures: List<CameramanPicture>
    private var listener: CameramanGalleryListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = CameramanGallery.getListener()
    }

    override fun onResume() {
        super.onResume()

        pictures = if (listener != null) listener?.onGallerySetupRecycler()!! else emptyList()

        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = GalleryAdapter(context!!, pictures, ::onItemClick)

        recycler.gallery(adapter)

        if (adapter.itemCount <= 0) txtNoPictures.visible(true) else txtNoPictures.visible(false)
    }

    private fun onItemClick(position: Int) {
        val intent = Intent(activity, ImageViewerActivity::class.java)
            .putExtra(Cameraman.POSITION, position)
            .putExtra(Cameraman.PICTURES, pictures as Serializable)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        startActivity(intent)
    }
}
