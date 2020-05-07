package com.brasileiro.cameraman.listener

import android.view.View
import android.content.Context
import android.view.animation.Animation
import android.view.OrientationEventListener

/**
 * @author Lucas Cota
 * @since 31/07/2019 10:04
 */

internal class OrientationListener(
    context: Context,
    private val portraitAnim: Animation,
    private val landscapeAnim: Animation,
    private val reversePortraitAnim: Animation,
    private val reverseLandscapeAnim: Animation,
    private val viewsToRotate: Array<View>? = null,
    private val rotationListener: RotationListener? = null
) : OrientationEventListener(context) {

    companion object {
        const val ROTATION_O = 1
        const val ROTATION_90 = 2
        const val ROTATION_180 = 3
        const val ROTATION_270 = 4
    }

    var rotation: Int = 1

    override fun onOrientationChanged(orientation: Int) {
        when {

            // PORTRAIT
            (orientation < 35 || orientation > 325) && rotation != ROTATION_O -> {
                rotation = ROTATION_O

                viewsToRotate?.forEach {
                    it.startAnimation(portraitAnim)
                }

                rotationListener?.onRotationChanged(rotation)
            }

            // REVERSE PORTRAIT
            orientation in 146..214 && rotation != ROTATION_180 -> {
                rotation = ROTATION_180

                viewsToRotate?.forEach {
                    it.startAnimation(reversePortraitAnim)
                }

                rotationListener?.onRotationChanged(rotation)
            }

            // REVERSE LANDSCAPE
            orientation in 56..124 && rotation != ROTATION_270 -> {
                rotation = ROTATION_270

                viewsToRotate?.forEach {
                    it.startAnimation(reverseLandscapeAnim)
                }

                rotationListener?.onRotationChanged(rotation)
            }

            // LANDSCAPE
            orientation in 236..304 && rotation != ROTATION_90 -> {
                rotation = ROTATION_90

                viewsToRotate?.forEach {
                    it.startAnimation(landscapeAnim)
                }

                rotationListener?.onRotationChanged(rotation)
            }
        }
    }


    interface RotationListener {

        fun onRotationChanged(rotation: Int)
    }
}
