package com.example.oxygencondensator

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.widget.Toast

class BleConnection(val context: Context) {
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var btAdapter: BluetoothAdapter

    fun initBtAdapter(){
        val bManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }
    fun initBttAdapter(){
        val bManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }

    fun bluetoothState(){
        if(bAdapter?.isEnabled == true){
            Toast.makeText(context, "Bluetooth ON", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Bluetooth OFF", Toast.LENGTH_SHORT).show()
        }
    }




}