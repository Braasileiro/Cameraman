package com.brasileiro.cameraman.geo.converter

/**
 * @author Lucas Cota
 * @since 18/06/2019 12:27
 */

internal class LatLon2UTM {

    companion object {

        // Constants
        private const val k0 = 0.9996
        private const val flattening = 298.2572236
        private const val equatorialRadius = 6378137.0

        private var eastingValue: Double = 0.0
        private var latitudeZoneValue: Int = 0
        private var northingValue: Double = 0.0
        private var longitudeZoneValue: Int = 0

        // Calculated
        private var a = equatorialRadius
        private var f = 1 / flattening
        private var b = a * (1 - f)   // polar radius
        private var e = Math.sqrt(1 - Math.pow(b, 2.0) / Math.pow(a, 2.0))
        private var esq = 1 - b / a * (b / a)
        private var e0sq = e * e / (1 - Math.pow(e, 2.0))


        // Identifier Array
        private var digraphArrayN = charArrayOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'
        )


        /*
         * Converters
         */

        fun convertLatToUTM(latitude: Double, longitude: Double): String {
            verifyLatLon(latitude, longitude)
            convert(latitude, longitude)
            return String.format("%.2f", eastingValue)
        }

        fun convertLonToUTM(latitude: Double, longitude: Double): String {
            verifyLatLon(latitude, longitude)
            convert(latitude, longitude)
            return String.format("%.2f", northingValue)
        }

        fun convertLatLonToUTM(latitude: Double, longitude: Double): String {
            verifyLatLon(latitude, longitude)
            convert(latitude, longitude)
            return String.format("%d%c", longitudeZoneValue, digraphArrayN[latitudeZoneValue])
        }


        // Base Convert
        private fun convert(latitude: Double, longitude: Double) {
            val latRad = latitude * Math.PI / 180.0
            val utmz = 1 + Math.floor((longitude + 180) / 6) // utm zone
            val zcm = 3 + 6 * (utmz - 1) - 180 // central meridian of a zone
            var latz = 0.0 // zone A-B for below 80S

            // convert latitude to latitude zone
            if (latitude > -80 && latitude < 72) {
                latz = Math.floor((latitude + 80) / 8) + 2 // zones C-W
            } else {
                if (latitude > 72 && latitude < 84) {
                    latz = 21.0 // zone X
                } else {
                    if (latitude > 84) {
                        latz = 23.0 // zones Y-Z
                    }
                }
            }

            val n = a / Math.sqrt(1 - Math.pow(e * Math.sin(latRad), 2.0))
            val t = Math.pow(Math.tan(latRad), 2.0)
            val c = e0sq * Math.pow(Math.cos(latRad), 2.0)
            val a = (longitude - zcm) * Math.PI / 180.0 * Math.cos(latRad)

            // calculate M (USGS style)
            var m = latRad * (1.0 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5.0 * esq / 256.0)))
            m -= Math.sin(2.0 * latRad) * (esq * (3.0 / 8.0 + esq * (3.0 / 32.0 + 45.0 * esq / 1024.0)))
            m += Math.sin(4.0 * latRad) * (esq * esq * (15.0 / 256.0 + esq * 45.0 / 1024.0))
            m -= Math.sin(6.0 * latRad) * (esq * esq * esq * (35.0 / 3072.0))
            m *= a //Arc length along standard meridian

            // calculate easting
            var x =
                k0 * n * a * (1.0 + a * a * ((1.0 - t + c) / 6.0 + a * a * (5.0 - 18.0 * t + t * t + 72.0 * c - 58.0 * e0sq) / 120.0)) //Easting relative to CM
            x += 500000 // standard easting

            // calculate northing
            var y =
                k0 * (m + n * Math.tan(latRad) * (a * a * (1.0 / 2.0 + a * a * ((5.0 - t + 9.0 * c + 4.0 * c * c) / 24.0 + a * a * (61.0 - 58.0 * t + t * t + 600.0 * c - 330.0 * e0sq) / 720.0)))) // from the equator

            if (y < 0) {
                y += 10000000 // add in false northing if south of the equator
            }

            longitudeZoneValue = utmz.toInt()
            latitudeZoneValue = latz.toInt()
            eastingValue = x
            northingValue = y
        }


        // Validator
        private fun verifyLatLon(latitude: Double, longitude: Double) {
            if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0
                || longitude >= 180.0
            ) {
                throw IllegalArgumentException(
                    "Legal ranges: latitude [-90,90], longitude [-180,180)."
                )
            }
        }
    }
}
