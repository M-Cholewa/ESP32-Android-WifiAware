package com.example.wifiawaretest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.aware.AttachCallback
import android.net.wifi.aware.DiscoverySessionCallback
import android.net.wifi.aware.PeerHandle
import android.net.wifi.aware.SubscribeConfig
import android.net.wifi.aware.SubscribeDiscoverySession
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.aware.WifiAwareSession
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.materialswitch.MaterialSwitch


class MainActivity : AppCompatActivity() {
    val AWARE_FILE_SHARE_SERVICE_NAME = "SOME_SERVICE_NAME_"
    var awareSession: WifiAwareSession? = null

    inner class MyCbk : AttachCallback() {
        private lateinit var peerHandle: PeerHandle;
        private lateinit var discoverySession : SubscribeDiscoverySession
        inner class MyDiscoverySessionCallback : DiscoverySessionCallback() {
            override fun onSubscribeStarted(session: SubscribeDiscoverySession) {

                discoverySession = session
                logText("onSubscribeStarted called")
            }

            override fun onServiceDiscovered(
                _peerHandle: PeerHandle,
                serviceSpecificInfo: ByteArray,
                matchFilter: List<ByteArray>
            ) {
                val message = "Hello publisher"
                peerHandle = _peerHandle
                discoverySession.sendMessage(peerHandle, 0, message.encodeToByteArray())
                logText("onServiceDiscovered called")
                logText(">SUBSCRIBER: $message")
            }

            override fun onMessageSendSucceeded(messageId: Int) {
                super.onMessageSendSucceeded(messageId)
                logText("onMessageSendSucceeded called")
            }

            override fun onMessageSendFailed(messageId: Int) {
                super.onMessageSendFailed(messageId)
                logText("onMessageSendFailed called")
            }

            override fun onMessageReceived(peerHandle: PeerHandle?, message: ByteArray?) {
                super.onMessageReceived(peerHandle, message)
                logText("onMessageReceived called")
                logText(">PUBLISHER: "+message?.decodeToString())
            }

        }

        // if used before acquiring session will throw NPE
        fun sendMessage(message: String){
            discoverySession.sendMessage(peerHandle, 0, message.encodeToByteArray())
            logText(">SUBSCRIBER: $message")
        }

        @SuppressLint("MissingPermission")

        override fun onAttached(session: WifiAwareSession?) {
            logText("onAttached called")
            super.onAttached(session)
            awareSession = session

            val config: SubscribeConfig = SubscribeConfig.Builder()
                .setServiceName(AWARE_FILE_SHARE_SERVICE_NAME)
                .build()

            awareSession!!.subscribe(config, MyDiscoverySessionCallback(), null)
        }

        override fun onAttachFailed() {
            logText("onAttachFailed called")
            super.onAttachFailed()
            awareSession = null
        }

        override fun onAwareSessionTerminated() {
            logText("onAwareSessionTerminated called")
            super.onAwareSessionTerminated()
            awareSession = null
        }
    }

    fun logText(text: String) {
        val textView: TextView = findViewById(R.id.textView)
        val text2 = textView.text.toString() + text + "\n"
        textView.text = text2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val hasFeature = this.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE)

        logText("hasSystemFeature FEATURE_WIFI_AWARE: $hasFeature")

        val wifiAwareManager = this.getSystemService(Context.WIFI_AWARE_SERVICE) as WifiAwareManager?

// Define a requestPermessionLauncher using the RequestPermission contract
        val requestPermessionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logText("ACCESS_FINE_LOCATION is not granted")
            requestPermessionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        } else {
            logText("ACCESS_FINE_LOCATION is granted")
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            logText("NEARBY_WIFI_DEVICES is not granted")
            requestPermessionLauncher.launch(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            logText("NEARBY_WIFI_DEVICES is granted")
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var cbk  = MyCbk()
        var ledSwitch : MaterialSwitch = findViewById(R.id.switch1)

        ledSwitch.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                cbk.sendMessage("LED ON")
            }else{
                cbk.sendMessage("LED OFF")
            }
        }

        logText("All permissions OK..")
        logText("Attaching session..")



        wifiAwareManager!!.attach(cbk, null)
    }


}