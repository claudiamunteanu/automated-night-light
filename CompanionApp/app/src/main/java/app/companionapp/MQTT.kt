package app.companionapp

import android.content.ContentValues
import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception

class MQTT{
    var mqttAndroidClient: MqttAndroidClient
    var mqttConnectOptions: MqttConnectOptions = MqttConnectOptions()

    constructor(
        context: Context
    ){
        mqttConnectOptions.userName = BuildConfig.USERNAME
        mqttConnectOptions.password = BuildConfig.PASSWORD.toCharArray();

        val clientId = MqttClient.generateClientId()
        /* Create an MqttAndroidClient object and configure the callback. */
        mqttAndroidClient = MqttAndroidClient(
            context, BuildConfig.SERVER_URL, clientId
        )
    }

    fun connectMQTT(
        connectionLost: (Throwable)->Unit,
        messageArrived: (String, MqttMessage)->Unit,
        deliveryComplete: (IMqttDeliveryToken)->Unit,
        onSuccess: (IMqttToken)->Unit,
        onFailure: (IMqttToken, Throwable) -> Unit
    ){

        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                connectionLost(cause)
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                messageArrived(topic, message)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                deliveryComplete(token)
            }
        })

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    onSuccess(asyncActionToken)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    onFailure(asyncActionToken, exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribeTopic(topic: String?, onSuccess: (IMqttToken) -> Unit, onFailure: (IMqttToken, Throwable) -> Unit) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    onSuccess(asyncActionToken)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    onFailure(asyncActionToken, exception)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}