package com.brasileiro.cameraman.geo

import android.os.Bundle
import android.content.Context
import android.location.Location
import android.annotation.SuppressLint
import android.location.LocationManager
import android.location.LocationListener

import com.brasileiro.cameraman.BuildConfig
import com.brasileiro.cameraman.util.RunnableHandler
import com.brasileiro.cameraman.geo.converter.LatLon2UTM
import com.brasileiro.cameraman.geo.model.GeolocationOutput
import com.brasileiro.cameraman.geo.listener.GeolocationListener

/**
 * @author Lucas Cota
 * @since 18/06/2019 11:15
 */

@SuppressLint("MissingPermission")
internal class Geolocation(context: Context, private var listener: GeolocationListener) :
    LocationListener {

    companion object {
        private const val TIME_INTERVAL: Long = 30000 // Miliseconds
        private const val DISTANCE_RADIUS: Float = 10f // Meters
    }

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val latLon2UTM: LatLon2UTM by lazy {
        LatLon2UTM()
    }

    private var limitReached: Boolean = true
    private lateinit var limitHandler: RunnableHandler


    fun requestLocationUpdates(
        autoCoordinatesInDebugMode: Boolean,
        enableCoordinatesTimeLimitWarning: Boolean,
        coordinatesTimeLimit: Long
    ) {
        if (BuildConfig.DEBUG && autoCoordinatesInDebugMode) {
            listener.onLocationChanged(
                GeolocationOutput(
                    -19.918746486121794,
                    -43.927838086946906,
                    "23K",
                    "612216,24",
                    "9999642,26"
                )
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                TIME_INTERVAL,
                DISTANCE_RADIUS,
                this
            )

            if (enableCoordinatesTimeLimitWarning) {
                limitHandler = RunnableHandler({
                    when (limitReached) {
                        true -> listener.onLocationRequestTimeLimitReached()
                        false -> limitHandler.stop()
                    }
                }, coordinatesTimeLimit)

                limitHandler.scheduledAtFixedRate()
            }
        }
    }

    fun removeLocationUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            limitReached = false

            listener.onLocationChanged(
                GeolocationOutput(
                    location.latitude,
                    location.longitude,
                    latLon2UTM.convertLatLonToUTM(location.latitude, location.longitude),
                    latLon2UTM.convertLatToUTM(location.latitude, location.longitude),
                    latLon2UTM.convertLonToUTM(location.latitude, location.longitude)
                )
            )
        }
    }


    // Unused
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}
