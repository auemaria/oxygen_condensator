package com.example.bt_dev.bluetooth

import android.bluetooth.BluetoothAdapter
import android.util.Log

class BluetoothController(private val adapter: BluetoothAdapter) {
    private var connectThread: ConnectThread? = null
    fun connect(mac: String, listener: Listener){
        if(adapter.isEnabled && mac.isNotEmpty()){
            val device = adapter.getRemoteDevice(mac)
            Log.d("", device.toString())
            connectThread = ConnectThread(device, adapter, listener)
            connectThread?.start()
        }
    }
    fun sendMessage(message: String){
        connectThread?.sendMessage(message)
    }

    fun closeConnection(){
        connectThread?.closeConnection()
    }
    interface Listener{
        fun onReceive(message: String){

        }
    }
    companion object{
        const val BLUETOOTH_CONNECTED = "bluetooth_connected"
        const val BLUETOOTH_NO_CONNECTED = "bluetooth_no_connected"
    }

}