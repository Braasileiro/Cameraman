package com.brasileiro.cameraman.bitmap

import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.channels.FileChannel

import kotlin.Exception

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix

import io.fotoapparat.result.BitmapPhoto

/**
 * @author Lucas Cota
 * @since 21/06/2019 13:38
 */

internal fun Bitmap.saveBitmapToJPEG(file: File, jpegQuality: Int): Boolean {
    return compress(Bitmap.CompressFormat.JPEG, jpegQuality, FileOutputStream(file))
}

internal fun Bitmap.rotateBitmap(rotationDegrees: Float): Bitmap {
    val matrix = Matrix()

    matrix.postRotate(rotationDegrees)

    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

internal fun BitmapPhoto.optimizeBitmap(context: Context): Bitmap? {
    var tempFile: File? = null

    try {
        // Source Bitmap (Rotated)
        val sourceBitmap = bitmap.rotateBitmap(-this@optimizeBitmap.rotationDegrees.toFloat())


        // Bitmap Configurations
        val width = sourceBitmap.width
        val height = sourceBitmap.height
        val config = sourceBitmap.config


        // Temp file
        tempFile = File.createTempFile("buffer-", ".tmp", context.cacheDir)
        tempFile.deleteOnExit()


        // Optimized
        val randomAccessFile = RandomAccessFile(tempFile, "rw")
        val fileChannel = randomAccessFile.channel
        val mappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, (sourceBitmap.rowBytes * height).toLong())


        // Store source bitmap buffer and recycle (flush memory)
        sourceBitmap.copyPixelsToBuffer(mappedByteBuffer)
        sourceBitmap.recycle()


        // Copy source bitmap buffer to new mutable one
        val bitmap = Bitmap.createBitmap(width, height, config)
        mappedByteBuffer.position(0)
        bitmap.copyPixelsFromBuffer(mappedByteBuffer)


        // Flush
        fileChannel.close()
        randomAccessFile.close()

        return bitmap

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // Assert temp file delete (if not null)
        tempFile?.delete()
    }

    return null
}
