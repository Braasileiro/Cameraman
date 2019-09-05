package com.brasileiro.cameraman.gallery

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.extension.horizontal
import com.brasileiro.cameraman.model.CameramanPicture
import com.brasileiro.cameraman.gallery.adapter.ImageViewerAdapter

import kotlinx.android.synthetic.main.activity_image_viewer.*

/**
 * @author Lucas Cota
 * @since 25/06/2019 16:08
 */

@Suppress("UNCHECKED_CAST")
internal class ImageViewerActivity : AppCompatActivity() {

    private val position: Int by lazy {
        intent.getSerializableExtra(Cameraman.POSITION) as Int
    }

    private val pictures: List<CameramanPicture> by lazy {
        intent.getSerializableExtra(Cameraman.PICTURES) as List<CameramanPicture>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_viewer)

        setupRecycler()
    }

    private fun setupRecycler() {
        val adapter = ImageViewerAdapter(this, pictures)

        recycler.horizontal(this, adapter)

        PagerSnapHelper().attachToRecyclerView(recycler)

        recycler.layoutManager?.scrollToPosition(position)
    }
}
