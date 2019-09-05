package com.brasileiro.cameraman

import android.content.Intent
import android.content.Context

import com.brasileiro.cameraman.view.CameraActivity
import com.brasileiro.cameraman.model.CameramanSettings
import com.brasileiro.cameraman.listener.CameramanCallback

/**
 * @author Lucas Cota
 * @since 13/06/2019 17:19
 */

class Cameraman(private var context: Context, private var settings: CameramanSettings) {

    companion object {
        const val CAMERA_SETTINGS = "CAMERA_SETTINGS"
        const val POSITION = "POSITION"
        const val PICTURES = "PICTURES"

        private var callback: CameramanCallback? = null

        internal fun getCallback(): CameramanCallback? {
            return callback
        }
    }

    fun start(callback: CameramanCallback) {
        Cameraman.callback = callback

        context.startActivity(Intent(context, CameraActivity::class.java).putExtra(CAMERA_SETTINGS, settings))
    }
}
