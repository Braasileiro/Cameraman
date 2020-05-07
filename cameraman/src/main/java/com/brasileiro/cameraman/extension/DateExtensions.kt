package com.brasileiro.cameraman.extension

import com.brasileiro.cameraman.util.RunnableHandler
import com.brasileiro.cameraman.listener.DateListener

/**
 * @author Lucas Cota
 * @since 25/06/2019 11:42
 */

internal fun dateAutoUpdater(
    pattern: String,
    interval: Long = 1000,
    listener: DateListener
): RunnableHandler {

    return RunnableHandler({ listener.onDateChanged(dateFormatNow(pattern)) }, interval)
}
