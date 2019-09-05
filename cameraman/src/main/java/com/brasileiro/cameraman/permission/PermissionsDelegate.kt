package com.brasileiro.cameraman.permission

import android.app.Activity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

/**
 * @author Lucas Cota
 * @since 12/06/2019 12:23
 */

internal class PermissionsDelegate(private var activity: Activity, private val REQUEST_PERMISSIONS: List<String>) {

    companion object {
        private const val REQUEST_CODE = 10
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, REQUEST_PERMISSIONS.toTypedArray(), REQUEST_CODE)
    }

    fun hasPermissions(): Boolean {
        val permissions = HashSet<Int>()

        REQUEST_PERMISSIONS.forEach {
            permissions.add(ActivityCompat.checkSelfPermission(activity, it))
        }

        return !permissions.map { it == PackageManager.PERMISSION_GRANTED }.contains(false)
    }

    fun resultGranted(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return when {
            requestCode != REQUEST_CODE -> false

            grantResults.size < REQUEST_PERMISSIONS.size -> false

            !permissions.contentEquals(REQUEST_PERMISSIONS.toTypedArray()) -> false

            grantResults.map { it == PackageManager.PERMISSION_GRANTED }.contains(false) -> false

            else -> true
        }
    }
}
