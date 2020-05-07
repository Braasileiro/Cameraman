package com.brasileiro.cameraman.view

import android.Manifest
import android.os.Bundle
import android.graphics.Bitmap
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.appcompat.app.AlertDialog
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import io.fotoapparat.selector.*
import io.fotoapparat.Fotoapparat
import io.fotoapparat.result.BitmapPhoto
import io.fotoapparat.result.WhenDoneListener
import io.fotoapparat.characteristic.LensPosition
import io.fotoapparat.configuration.CameraConfiguration

import com.brasileiro.cameraman.R
import com.brasileiro.cameraman.bitmap.*
import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.extension.*
import com.brasileiro.cameraman.geo.Geolocation
import com.brasileiro.cameraman.extension.toast
import com.brasileiro.cameraman.util.RunnableHandler
import com.brasileiro.cameraman.geo.util.Coordinator
import com.brasileiro.cameraman.listener.DateListener
import com.brasileiro.cameraman.model.CameramanOutput
import com.brasileiro.cameraman.extension.createDialog
import com.brasileiro.cameraman.model.CameramanSettings
import com.brasileiro.cameraman.bitmap.saveBitmapToJPEG
import com.brasileiro.cameraman.selector.CoordinateType
import com.brasileiro.cameraman.listener.CameramanCallback
import com.brasileiro.cameraman.geo.model.GeolocationOutput
import com.brasileiro.cameraman.selector.resolutionSelector
import com.brasileiro.cameraman.listener.OrientationListener
import com.brasileiro.cameraman.permission.PermissionsDelegate
import com.brasileiro.cameraman.geo.listener.GeolocationListener

import kotlinx.android.synthetic.main.content_camera.*
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.content_camera_preview.*

/**
 * @author Lucas Cota
 * @since 11/06/2019 16:36
 */

internal class CameraActivity : AppCompatActivity(), OrientationListener.RotationListener,
    GeolocationListener, DateListener {

    companion object {
        private val PERMISSIONS = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        private val PERMISSIONS_COORDINATES = arrayListOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    // Orientation
    private lateinit var orientationListener: OrientationListener


    // Permissions
    private lateinit var permissionsDelegate: PermissionsDelegate


    // Date
    private lateinit var dateOutput: String
    private lateinit var dateHandler: RunnableHandler


    // Location
    private var geolocation: Geolocation? = null
    private var locationOutput: GeolocationOutput? = null


    // Description
    private var descriptionOutput: String = ""


    // Camera
    private var isFlash = false
    private var isBackCamera = true
    private lateinit var bitmap: Bitmap
    private lateinit var fotoapparat: Fotoapparat
    private lateinit var configuration: CameraConfiguration


    // Settings
    private val settings: CameramanSettings by lazy {
        intent.getSerializableExtra(Cameraman.CAMERA_SETTINGS) as CameramanSettings
    }


    // Callback
    private lateinit var callback: CameramanCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)

        supportActionBar?.hide()

        configureOrientation()

        checkPermissions()

        configureCamera()
    }

    override fun onStart() {
        super.onStart()

        orientationListener.enable()

        if (permissionsDelegate.hasPermissions()) startFotoapparat()
    }

    override fun onStop() {
        super.onStop()

        orientationListener.disable()

        if (permissionsDelegate.hasPermissions()) stopFotoapparat()
    }

    override fun onBackPressed() {
        if (contentCameraPreview.isVisible) {
            switchToCamera()
        } else {
            super.onBackPressed()
            callback.onPreviewBackPressed()
        }
    }


    // Orientation
    private fun configureOrientation() {
        val portraitAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_to_portrait)
        val landscapeAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_to_landscape)
        val reversePortraitAnim =
            AnimationUtils.loadAnimation(this, R.anim.rotate_to_reverse_portrait)
        val reverseLandscapeAnim =
            AnimationUtils.loadAnimation(this, R.anim.rotate_to_reverse_landscape)

        orientationListener = OrientationListener(
            this,
            portraitAnimation,
            landscapeAnimation,
            reversePortraitAnim,
            reverseLandscapeAnim,
            arrayOf(flashSwitch, switchCamera),
            this
        )
    }

    override fun onRotationChanged(rotation: Int) {
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        when (rotation) {
            OrientationListener.ROTATION_O -> {
                params.apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                }

                setCoordinatesViewRotation(0, params)
            }

            OrientationListener.ROTATION_180 -> {
                params.apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                }

                setCoordinatesViewRotation(-180, params)
            }

            OrientationListener.ROTATION_270 -> {
                params.apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                }

                setCoordinatesViewRotation(90, params)
            }

            OrientationListener.ROTATION_90 -> {
                params.apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                }

                setCoordinatesViewRotation(-90, params)
            }
        }
    }


    // Date
    override fun onDateChanged(date: String) {
        dateOutput = date

        runOnUiThread { txtDate.text = date }
    }


    // Location
    override fun onLocationChanged(output: GeolocationOutput) {
        checkCoordinatesView()

        locationOutput = output

        when (settings.coordinateType) {

            CoordinateType.UTM -> {
                txtUtm.text = locationOutput?.utm
                txtUtmX.text = locationOutput?.utmX
                txtUtmY.text = locationOutput?.utmY
            }

            CoordinateType.LAT_LNG -> {
                txtUtmX.text = locationOutput?.latitude.toString()
                txtUtmY.text = locationOutput?.longitude.toString()
            }
        }
    }

    override fun onLocationRequestTimeLimitReached() {
        runOnUiThread { toast(getString(R.string.camera_location_request_time_limit)) }
    }

    // Camera
    private fun startFotoapparat() {
        fotoapparat.start()
        dateHandler.scheduledAtFixedRate()

        if (settings.enableCoordinates) geolocation?.requestLocationUpdates(
            settings.enableAutoCoordinatesInDebugMode,
            settings.enableCoordinatesTimeLimitWarning,
            settings.coordinatesTimeLimitWarning
        )
    }

    private fun stopFotoapparat() {
        fotoapparat.stop()
        dateHandler.stop()

        if (settings.enableCoordinates) geolocation?.removeLocationUpdates()
    }

    private fun configureCamera() {
        callback = Cameraman.getCallback()!!

        configuration = CameraConfiguration(
            flashMode = off(),
            pictureResolution = resolutionSelector(settings.resolutionSelector)
        )

        fotoapparat = Fotoapparat(
            this,
            view = cameraView,
            focusView = if (settings.enableFocusView) focusView else null,
            lensPosition = back(),
            cameraConfiguration = configuration
        )

        if (settings.enableCoordinates) {

            if (settings.coordinateType == CoordinateType.LAT_LNG) {
                rowUtm.visible(false)

                titleUtmX.text = getString(R.string.camera_latitude)
                titleUtmY.text = getString(R.string.camera_longitude)
            }

            geolocation = Geolocation(this, this)
        }

        if (settings.enableDescription) {
            txtDescription.visible(true)
        }

        dateHandler = dateAutoUpdater(pattern = settings.datePattern, listener = this)

        initializeCameraListeners()
    }

    private fun initializeCameraListeners() {
        /*
         * Camera
         */

        // Switch Camera
        switchCamera.setOnClickListener {
            if (hasFrontCamera()) {
                isBackCamera = !isBackCamera

                fotoapparat.switchTo(
                    lensPosition = when {
                        isBackCamera -> {
                            switchCamera.setImageResource(R.drawable.ic_camera_front_white)
                            back()
                        }

                        else -> {
                            switchCamera.setImageResource(R.drawable.ic_camera_rear_white)
                            front()
                        }
                    },

                    cameraConfiguration = when {
                        isBackCamera -> configuration

                        else -> configuration.copy(
                            pictureResolution = highestResolution()
                        )
                    }
                )
            } else {
                parentView.snackbar(getString(R.string.camera_no_switch_available))
            }
        }

        // Flash Switch
        flashSwitch.setOnClickListener {
            isFlash = !isFlash

            fotoapparat.updateConfiguration(
                configuration.copy(
                    flashMode = when {
                        isFlash -> {
                            flashSwitch.setImageResource(R.drawable.ic_flash_on)
                            firstAvailable(on(), off())
                        }

                        else -> {
                            flashSwitch.setImageResource(R.drawable.ic_flash_off_white)
                            off()
                        }
                    }
                )
            )
        }

        // Capture
        capturePicture.setOnClickListener {
            if (settings.enableCoordinates && loadingCoordinatesView.isVisible) {
                toast(getString(R.string.camera_wait_coordinates))

                return@setOnClickListener
            }

            switchActionView(false)

            fotoapparat.takePicture().toBitmap().whenDone(object : WhenDoneListener<BitmapPhoto> {
                override fun whenDone(it: BitmapPhoto?) {

                    if (it == null) {
                        throw Exception("Couldn't capture photo.")
                    }

                    val newBitmap = it.optimizeBitmap(this@CameraActivity)
                        ?: throw Exception("Couldn't optimize bitmap.")

                    bitmap = when {
                        settings.enableCoordinates -> Coordinator.plotCoordinatesIntoBitmap(
                            this@CameraActivity,
                            newBitmap,
                            locationOutput!!,
                            dateOutput,
                            settings.coordinateType
                        )

                        else -> newBitmap
                    }

                    switchToPreview()
                }
            })
        }


        /*
         * Preview
         */

        // Repeat
        txtRepeat.setOnClickListener {
            descriptionOutput = ""

            switchToCamera()
        }

        // Description
        txtDescription.setOnClickListener {
            val alertDialog = createDialog(
                context = this,
                view = R.layout.dialog_camera_description,
                title = getString(R.string.camera_edit_description),
                negativeText = getString(R.string.camera_cancel)
            )

            alertDialog.setPositiveButton(getString(R.string.camera_confirm)) { dialog, _ ->
                descriptionOutput =
                    (dialog as AlertDialog).findViewById<EditText>(R.id.edtDescription)?.toText()!!
            }

            val dialog = alertDialog.create()

            dialog.show()

            dialog.findViewById<EditText>(R.id.edtDescription)?.setText(descriptionOutput)
        }

        // Confirm
        txtConfirm.setOnClickListener {
            if (!settings.savePath.fileExists()) settings.savePath.mkdirs()

            val picture = settings.savePath.saveFileWithTimestamp(".jpg")

            bitmap.saveBitmapToJPEG(picture, settings.jpegQuality)

            finish()

            callback.onPreviewConfirmed(
                CameramanOutput(
                    picture,
                    dateOutput,
                    settings.datePattern,
                    "${settings.descriptionPrefix}$descriptionOutput",
                    locationOutput
                )
            )
        }
    }


    // Switchers
    private fun switchToCamera() {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        startFotoapparat()

        switchActionView(true)
        contentCamera.visible(true)
        contentCameraPreview.visible(false)
    }

    private fun switchToPreview() {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        stopFotoapparat()

        contentCamera.visible(false)
        contentCameraPreview.visible(true)

        imagePreview.setImageBitmap(bitmap)
    }

    private fun enableCameraView() {
        cameraView.visible(true)
        actionView.visible(true)
        loadingCoordinatesView.visible(settings.enableCoordinates)
    }

    private fun switchActionView(isEnabled: Boolean) {
        flashSwitch.isEnabled = isEnabled
        capturePicture.isEnabled = isEnabled
        switchCamera.isEnabled = isEnabled
    }

    private fun checkCoordinatesView() {
        when {
            loadingCoordinatesView.isVisible -> {
                loadingCoordinatesView.visible(false)
                coordinatesView.visible(true)
            }
        }
    }

    private fun setCoordinatesViewRotation(angle: Int, params: ConstraintLayout.LayoutParams) {
        when {
            loadingCoordinatesView.isVisible -> {
                loadingCoordinatesView.angle = angle
                loadingCoordinatesView.layoutParams = params
            }
        }

        coordinatesView.angle = angle
        coordinatesView.layoutParams = params
    }


    // Validators
    private fun hasFrontCamera(): Boolean {
        return fotoapparat.isAvailable { LensPosition.Front }
    }


    // Permissions
    private fun checkPermissions() {
        if (settings.enableCoordinates) PERMISSIONS.addAll(PERMISSIONS_COORDINATES)

        permissionsDelegate = PermissionsDelegate(
            this,
            PERMISSIONS
        )

        when {
            permissionsDelegate.hasPermissions() -> enableCameraView()

            else -> permissionsDelegate.requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when {
            permissionsDelegate.resultGranted(requestCode, permissions, grantResults) -> {
                txtNoPermission.visible(false)

                startFotoapparat()

                enableCameraView()
            }

            else -> txtNoPermission.visible(true)
        }
    }
}
