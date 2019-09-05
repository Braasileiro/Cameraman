package com.brasileiro.cameraman.extension

import java.io.File
import java.util.Date
import java.util.Locale
import java.text.SimpleDateFormat

/**
 * @author Lucas Cota
 * @since 14/06/2019 10:45
 */

internal fun String.fileExists(): Boolean {
    return File(this).exists()
}

internal fun String.mkdirs(): Boolean {
    return File(this).mkdirs()
}

internal fun String.saveFileWithTimestamp(suffix: String): File {
    return File(
        this,
        String.format("%s$suffix", SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date()))
    )
}

internal fun Collection<String>.maximumLenghtString(): String {
    var string = ""

    this
        .asSequence()
        .filter { it.length > string.length }
        .forEach { string = it }

    return string
}

internal fun dateFormatNow(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
}
