package com.brasileiro.cameraman.gallery.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.extension.toast
import com.brasileiro.cameraman.extension.fileExists
import com.brasileiro.cameraman.model.CameramanPicture

import kotlinx.android.synthetic.main.item_gallery.view.*

/**
 * @author Lucas Cota
 * @since 19/06/2019 17:16
 */

internal class GalleryAdapter(
    val context: Context, private val itens: List<CameramanPicture>, private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = itens.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val foto = itens[position]

        if (foto.path.fileExists()) {
            Glide.with(context)
                .load(foto.path)
                .into(holder.itemView.picture)

            holder.itemView.setOnClickListener { onItemClick(position) }

            holder.itemView.txtDescription.text = foto.description.orEmpty()
        } else {
            holder.itemView.setOnClickListener {
                context.toast(context.getString(R.string.camera_gallery_picture_not_found))
            }
        }
    }
}
