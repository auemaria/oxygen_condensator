package com.example.bt_dev.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import java.io.IOException
import java.util.UUID

class ConnectThread(val device: BluetoothDevice, val adapter: BluetoothAdapter, val listener: BluetoothController.Listener): Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"

    private var mSocket: BluetoothSocket? = null
    init{

        try{
//            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
            Log.d("", "Дабоя")
        } catch (e: IOException){
        } catch(se: SecurityException){
            checkAccess()
        }
    }

    override fun run() {
        try{
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
            adapter.cancelDiscovery()
            mSocket?.connect()
            listener.onReceive(BluetoothController.BLUETOOTH_CONNECTED)
            Log.d("", "Connected")
            readMessage()
        } catch (e: IOException){
            Log.d("", "Not connected")
            Log.d("", e.printStackTrace().toString())
            listener.onReceive(BluetoothController.BLUETOOTH_NO_CONNECTED)

        } catch(se: SecurityException){
            checkAccess()
        }
    }

    private fun readMessage(){
        val buffer = ByteArray(250000)
        while(true){
            try{
                val lenght = mSocket?.inputStream?.read(buffer)
                val message = String(buffer,0,lenght ?: 0)
                Log.d("MessageBLE", message)
                listener.onReceive(message)
            }catch (e: IOException){
                break
            }
        }
    }

    fun sendMessage(message: String){
        try{
            mSocket?.outputStream?.write(message.toByteArray())
        }catch (e:IOException){

        }
    }

    fun closeConnection(){
        try{
            mSocket?.close()

        } catch (e: IOException){

        }
    }
}