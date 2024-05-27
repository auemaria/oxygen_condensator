package com.example.bt_dev

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


class CheckBt : Fragment() {
    private var bAdapter: BluetoothAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtAdapter()
        bluetoothState()
    }

    private fun initBtAdapter(){
        val bManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }

    private fun bluetoothState(){
        if(bAdapter?.isEnabled == true){
            Toast.makeText(context, "Bluetooth ON", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Bluetooth OFF", Toast.LENGTH_SHORT).show()
        }
    }

}