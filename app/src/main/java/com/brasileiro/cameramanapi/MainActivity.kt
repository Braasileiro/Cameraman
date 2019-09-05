package com.brasileiro.cameramanapi

import android.os.Bundle
import android.widget.Toast
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity

import com.brasileiro.cameraman.Cameraman
import com.brasileiro.cameraman.model.CameramanOutput
import com.brasileiro.cameraman.model.CameramanSettings
import com.brasileiro.cameraman.listener.CameramanCallback

import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Lucas Cota
 * @since 31/07/2019 15:59
 */

class MainActivity : AppCompatActivity(), CameramanCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnCamera.setOnClickListener {
            Cameraman(
                this, CameramanSettings(
                    savePath = "${Environment.getExternalStorageDirectory()}/${getString(R.string.app_name)}",
                    enableCoordinates = true,
                    enableDescription = true,
                    datePattern = "yyyy/MM/dd HH:mm:ss"
                )
            ).start(this)
        }
    }

    override fun onPreviewConfirmed(output: CameramanOutput) {
        Toast.makeText(this, output.toString(), Toast.LENGTH_LONG).show()
    }
}
