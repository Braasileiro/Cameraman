package com.brasileiro.cameraman.model

import java.io.Serializable

import com.brasileiro.cameraman.selector.CoordinateType
import com.brasileiro.cameraman.selector.ResolutionQuality

/**
 * @author Lucas Cota
 * @since 18/06/2019 14:43
 */

data class CameramanSettings(
    var savePath: String,
    var resolutionSelector: ResolutionQuality = ResolutionQuality.MEDIUM,
    var jpegQuality: Int = 90,
    var enableFocusView: Boolean = false,
    var enableCoordinates: Boolean = false,
    var enableAutoCoordinatesInDebugMode: Boolean = false,
    var coordinateType: CoordinateType = CoordinateType.UTM,
    var enableDescription: Boolean = false,
    var datePattern: String,
    var descriptionPrefix: String = ""
) : Serializable
