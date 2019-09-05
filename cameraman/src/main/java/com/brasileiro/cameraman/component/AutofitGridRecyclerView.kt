package com.brasileiro.cameraman.component

import android.content.Context
import android.util.AttributeSet

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

/**
 * @author Lucas Cota
 * @since 24/06/2019 09:02
 */

internal class AutofitGridRecyclerView : RecyclerView {

    private var columnWidth = -1
    private lateinit var manager: GridLayoutManager


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)

            val typedArray = context.obtainStyledAttributes(attrs, attrsArray)

            columnWidth = typedArray.getDimensionPixelSize(0, -1)

            typedArray.recycle()
        }

        manager = GridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)

        if (columnWidth > 0) manager.spanCount = Math.max(1, measuredWidth / columnWidth)
    }
}
