package com.brasileiro.cameraman.extension

import android.content.Context

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * @author Lucas Cota
 * @since 23/08/2019 10:14
 */

internal fun <T : RecyclerView.ViewHolder> RecyclerView.gallery(
    adapter: RecyclerView.Adapter<T>
): RecyclerView {
    this.setHasFixedSize(true)
    this.adapter = adapter

    return this
}

internal fun <T : RecyclerView.ViewHolder> RecyclerView.horizontal(
    context: Context,
    adapter: RecyclerView.Adapter<T>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    )
): RecyclerView {
    this.layoutManager = layoutManager
    this.adapter = adapter

    return this
}
