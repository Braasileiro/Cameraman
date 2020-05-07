package com.brasileiro.cameraman.gallery.view.album

import java.io.Serializable

import kotlin.properties.Delegates

import android.os.Build
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.extension.visible
import com.brasileiro.cameraman.extension.gallery
import com.brasileiro.cameraman.gallery.CameramanGallery
import com.brasileiro.cameraman.gallery.adapter.AlbumAdapter
import com.brasileiro.cameraman.gallery.model.CameramanAlbum
import com.brasileiro.cameraman.gallery.adapter.PictureAdapter
import com.brasileiro.cameraman.gallery.listener.CameramanGalleryListener
import com.brasileiro.cameraman.gallery.view.imageviewer.ImageViewerActivity

import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * @author Lucas Cota
 * @since 23/08/2019 10:24
 */

@Suppress("UNCHECKED_CAST")
class FragmentAlbum : Fragment() {

    private var root: View by Delegates.notNull()

    private val listener: CameramanGalleryListener<CameramanAlbum> by lazy {
        CameramanGallery.listener as CameramanGalleryListener<CameramanAlbum>
    }

    private val parentActivity: AppCompatActivity by lazy { activity as AppCompatActivity }


    private var isAlbum: Boolean = true
    private lateinit var album: CameramanAlbum
    private lateinit var items: List<CameramanAlbum>


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

    override fun onDestroy() {
        super.onDestroy()

        parentActivity.supportActionBar?.subtitle = null
    }

    fun onBackPressed(): Boolean {
        if (!isAlbum) {
            isAlbum = true

            setupRecycler()

            parentActivity.supportActionBar?.subtitle = null

            return false
        }

        return true
    }

    private fun setupRecycler() {
        if (isAlbum) {
            recycler.gallery(AlbumAdapter(context!!, items, ::onItemClick))
        } else {
            recycler.gallery(PictureAdapter(context!!, album.pictures, ::onPictureClick))
        }

        if (items.isNullOrEmpty()) txtNoPictures.visible(true) else txtNoPictures.visible(false)
    }

    private fun onItemClick(album: CameramanAlbum) {
        isAlbum = false

        this.album = album

        parentActivity.supportActionBar?.subtitle = album.title

        setupRecycler()
    }

    private fun onPictureClick(position: Int) {
        val intent = Intent(activity, ImageViewerActivity::class.java)
            .putExtra(Cameraman.POSITION, position)
            .putExtra(Cameraman.PICTURES, album.pictures as Serializable)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        startActivity(intent)
    }
}
