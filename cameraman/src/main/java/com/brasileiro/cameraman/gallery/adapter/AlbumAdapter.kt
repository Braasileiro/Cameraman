package com.brasileiro.cameraman.gallery.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.widget.ImageView
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.extension.fileExists
import com.brasileiro.cameraman.gallery.model.CameramanAlbum

import kotlinx.android.synthetic.main.item_album.view.*

/**
 * @author Lucas Cota
 * @since 13/01/2020 12:49
 */

internal class AlbumAdapter(
    val context: Context, private val items: List<CameramanAlbum>,
    private val onItemClick: (CameramanAlbum) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val album = items[position]

        val views = listOf<ImageView>(
            holder.itemView.picture1,
            holder.itemView.picture2,
            holder.itemView.picture3,
            holder.itemView.picture4
        )

        holder.itemView.txtDescription.text = album.title.orEmpty()

        run makeAlbum@{
            album.pictures.forEachIndexed { index, picture ->
                if (picture.path.fileExists()) {
                    Glide.with(context).load(picture.path).into(views[index])
                } else {
                    Glide.with(context).load(android.R.drawable.ic_menu_report_image)
                        .into(views[index])
                }

                if (index == 3) return@makeAlbum
            }
        }

        holder.itemView.setOnClickListener { onItemClick(album) }
    }
}
