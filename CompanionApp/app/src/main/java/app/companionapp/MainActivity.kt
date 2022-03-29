package app.companionapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.eclipse.paho.client.mqttv3.*

import android.hardware.camera2.CameraManager
import android.widget.CompoundButton

import android.app.AlertDialog
import android.content.Context

import android.hardware.camera2.CameraAccessException

import android.content.pm.PackageManager
import android.content.DialogInterface


class MainActivity : AppCompatActivity() {

    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null

    var flashlightOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mqtt = MQTT(this)
        mqtt.connectMQTT(
            connectionLost = {
                Log.e(ContentValues.TAG, "connection lost", it)
            },
            messageArrived = { topic: String, message: MqttMessage ->
                val messageString = String(message.payload)
                Log.i(ContentValues.TAG, "topic: $topic, msg: $messageString")
                val enableFlashlight = messageString.toBoolean()
                switchFlashLight(enableFlashlight)
            },
            deliveryComplete = {
                Log.i(ContentValues.TAG, "msg delivered")
            },
            onSuccess = {
                Log.i(ContentValues.TAG, "connect succeed")
                mqtt.subscribeTopic(
                    topic = "raspberry/flashlight",
                    onSuccess = {
                        Log.i(ContentValues.TAG, "subscribed succeed")
                    },
                    onFailure = {asyncActionToken: IMqttToken, exception: Throwable ->
                        Log.i(ContentValues.TAG, "subscribed failed", exception)
                    })
            },
            onFailure = { asyncActionToken: IMqttToken, exception: Throwable ->
                Log.e(ContentValues.TAG, "connect failed", exception)
            }
        )

        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        if (!isFlashAvailable) {
            showNoFlashError()
        }

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager!!.getCameraIdList()[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun showNoFlashError() {
        val alert: AlertDialog = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(
            DialogInterface.BUTTON_POSITIVE, "OK"
        ) { _, _ -> finish() }
        alert.show()
    }

    private fun switchFlashLight(value: Boolean) {
        try {
            flashlightOn = value
            mCameraManager!!.setTorchMode(mCameraId!!, flashlightOn)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}