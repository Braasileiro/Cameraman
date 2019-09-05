package com.brasileiro.cameraman.geo.util

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.content.Context

import androidx.core.content.res.ResourcesCompat

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.selector.CoordinateType
import com.brasileiro.cameraman.geo.model.GeolocationOutput
import com.brasileiro.cameraman.extension.maximumLenghtString

/**
 * @author Lucas Cota
 * @since 21/06/2019 12:04
 */

internal class Coordinator {

    companion object {
        private const val DEFAULT_MARGIN = 10f
        private const val DEFAULT_PADDING = 63f
        private const val DEFAULT_TEXT_SIZE = 50f

        fun plotCoordinatesIntoBitmap(
            context: Context, bitmap: Bitmap, locationOutput: GeolocationOutput, date: String, type: CoordinateType
        ): Bitmap {

            // Resources
            val paint = Paint()
            val canvas = Canvas(bitmap)
            val plotStringArray = locationOutput.toPlotString(context, date, type).split("\n")


            // Bitmap
            canvas.save()
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            canvas.restore()


            // Paint
            paint.textSize = DEFAULT_TEXT_SIZE
            paint.typeface = ResourcesCompat.getFont(context, R.font.robotomedium)


            // Rect (semi-transparent)
            paint.color = Color.parseColor("#99fdfdfd")

            canvas.drawRect(
                0f,
                0f,
                paint.measureText(plotStringArray.maximumLenghtString()) + DEFAULT_MARGIN,
                (DEFAULT_PADDING * plotStringArray.size) + DEFAULT_MARGIN,
                paint
            )


            // Text (red)
            var padding = 0f

            paint.color = Color.parseColor("#ffff0000")

            for (line in plotStringArray) {
                padding += DEFAULT_PADDING

                canvas.drawText(line, DEFAULT_MARGIN, padding, paint)
            }

            return bitmap
        }
    }
}
