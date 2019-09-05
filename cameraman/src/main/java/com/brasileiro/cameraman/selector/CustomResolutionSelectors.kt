package com.brasileiro.cameraman.selector

import io.fotoapparat.selector.lowestResolution
import io.fotoapparat.selector.highestResolution
import io.fotoapparat.selector.ResolutionSelector

/**
 * @author Lucas Cota
 * @since 13/06/2019 14:53
 */

enum class ResolutionQuality {
    BEST,
    HIGH,
    MEDIUM,
    LOW,
    SAD
}

internal fun resolutionSelector(quality: ResolutionQuality): ResolutionSelector {
    return when (quality) {
        ResolutionQuality.BEST -> highestResolution()
        ResolutionQuality.HIGH -> highResolution()
        ResolutionQuality.MEDIUM -> mediumResolution()
        ResolutionQuality.LOW -> lowResolution()
        ResolutionQuality.SAD -> lowestResolution()
    }
}

private fun highResolution(): ResolutionSelector = {
    val resolutions = this.sortedByDescending { it.area }

    val half = resolutions.size / 2
    val index = (resolutions.size - half) / 2

    resolutions[index]
}

private fun mediumResolution(): ResolutionSelector = {
    val resolutions = this.sortedByDescending { it.area }

    val index = resolutions.size / 2

    resolutions[index]
}

private fun lowResolution(): ResolutionSelector = {
    val resolutions = this.sortedByDescending { it.area }

    val half = resolutions.size / 2
    val index = half + (half / 2)

    resolutions[index]
}
