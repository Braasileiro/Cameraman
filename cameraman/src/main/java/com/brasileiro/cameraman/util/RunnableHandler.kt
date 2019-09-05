package com.brasileiro.cameraman.util

import android.os.Handler

import kotlinx.coroutines.Runnable

/**
 * @author Lucas Cota
 * @since 25/06/2019 14:36
 */

internal class RunnableHandler(private var method: () -> Unit, var interval: Long = 0) {

    // Uuu boi
    private var killMinus9: Boolean = false


    /*
     * Handler
     */
    private val handler: Handler = Handler()


    /*
     * Runnable
     */
    private val runnable: Runnable by lazy {
        Runnable {
            if (killMinus9) return@Runnable

            method()
        }
    }

    private val fixedRateRunnable: Runnable by lazy {
        Runnable {
            if (killMinus9) return@Runnable

            method()

            handler.postDelayed(fixedRateRunnable, interval)
        }
    }


    /*
     * Run Modes
     */
    fun stop() {
        killMinus9 = true

        handler.removeCallbacks(null)
    }

    fun runOnce() {
        killMinus9 = false

        handler.post(runnable)
    }

    fun schedule() {
        killMinus9 = false

        handler.postDelayed(runnable, interval)
    }

    fun scheduledAtFixedRate() {
        killMinus9 = false

        handler.postDelayed(fixedRateRunnable, interval)
    }
}
