package com.example.oxygencondensator

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.bt_dev.bluetooth.BluetoothController
import java.io.File
import java.io.IOException
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CondensatorActivity : ComponentActivity(), BluetoothController.Listener {
    private lateinit var btLauncher: ActivityResultLauncher<Intent>
    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var bAdapter: BluetoothAdapter
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var bluetoothController: BluetoothController
    private lateinit var chronometer: Chronometer
    private lateinit var textSP02: TextView
    private lateinit var textPID: TextView
    private lateinit var plusbutton: ImageButton
    private lateinit var minusbutton: ImageButton
    private lateinit var file: File
    private lateinit var autocheckbox: CheckBox
    private lateinit var user_login: String
    val bt = BleConnection(this)
    var isfile = false
    private lateinit var user_pass: String
    private val internalStorageManager: FileHelper? = Storage.instance?.internalStorage
    var spo2: Int = 0
    var litr: Double = 0.0
    var elapsedMillis: Long = 0
    var litrl:Double = 0.0
    var count: Int = 0
    var info: MutableList<String> = mutableListOf();
    var msg: String = "";
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condensator)
        val buttonConnect: Button = findViewById(R.id.bconnect)
        val endbutton: Button = findViewById(R.id.endbutton)
        autocheckbox = findViewById(R.id.autocheckBox)
        plusbutton= findViewById(R.id.image_plus)
        minusbutton = findViewById(R.id.image_minus)
        plusbutton.setClickable(false)
        minusbutton.setClickable(false)
        chronometer = findViewById(R.id.chronometer)
        autocheckbox.isChecked = true
        autocheckbox.isClickable = false
//        val buttonSend: Button = findViewById(R.id.sbutton)
        textSP02 = findViewById(R.id.tsend)
        textPID = findViewById(R.id.bsend1)
        initBtAdapter()
        initBttAdapter()
        checkPermissions()
        registerBtLauncher()

        btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

        bt.bluetoothState()



        bluetoothController = BluetoothController(btAdapter)

        val device: BluetoothDevice = btAdapter.getRemoteDevice("98:D3:31:B2:38:03")
        try{
            device.createBond()
            Log.d("","Победа!")
        }catch (e:IOException){
            Log.d("","Пизда")
        }catch (sp: SecurityException){
            Log.d("","Пизда")
        }

        autocheckbox.setOnCheckedChangeListener(){ buttonView, isChecked ->
            if(isChecked){
                plusbutton.setClickable(false)
                minusbutton.setClickable(false)

                try {
                    file.appendText("Автоматический режим активирован\n")
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
            else{
                plusbutton.setClickable(true)
                minusbutton.setClickable(true)
                litr = litrl
                textPID.setText(litr.toString())
                try {
                    file.appendText("Автоматический режим деактивирован\n")
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }
        plusbutton.setOnClickListener(){
            litr = litr + 0.1
            textPID.setText(String.format("%.1f", litr))
        }
        minusbutton.setOnClickListener(){
            litr = litr - 0.1
            textPID.setText(String.format("%.1f", litr))
        }
        buttonConnect.setOnClickListener{
            bluetoothController.connect("00:18:E4:08:0F:01", this)
        }
        endbutton.setOnClickListener{
            val db = DBhelper(this,null)
            user_login = intent.extras?.getString("login").toString()
            user_pass = intent.extras?.getString("pass").toString()
            chronometer.stop()
            elapsedMillis = (SystemClock.elapsedRealtime() - chronometer.base)
            Log.d("clock",(LocalDate.now()).toString())

            file = internalStorageManager!!.getfile(this, ((LocalDate.now()).toString())+".txt")!!
            if(isfile){
//                Storage.instance?.internalStorage?.write(
//                    file,  "", 3)
                if(db.getTherapyInfo(user_login,user_pass)[4] == "0"){
                    val isfilecreated = internalStorageManager!!.createFile(this, "statistic" + user_login.toString() +".txt")
                    val file2 = internalStorageManager!!.getfile(this, "statistic" + user_login.toString() +".txt")!!
                    file2.appendText("Therapy " + (LocalDate.now()).toString() +" "+ LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm")).toString() + " Средняя сатурация: " +(spo2/count).toString() + " Длительность: " + (elapsedMillis/60000).toInt().toString() + " минут\n")
                }
                else{
                    val file2 = internalStorageManager!!.getfile(this, "statistic" + user_login.toString() +".txt")!!
                    if(file2.exists()) {
//                        Storage.instance?.internalStorage?.write(
//                            file2, "Therapy " + (LocalDate.now()).toString() + " "+ LocalDateTime.now().format(
//                                DateTimeFormatter.ofPattern("HH:mm")).toString(), 0
//                        )
//                        Storage.instance?.internalStorage?.write(
//                            file2, "Therapy " + (LocalDate.now()).toString() +" "+ LocalDateTime.now().format(
//                                DateTimeFormatter.ofPattern("HH:mm")).toString() + "Средняя сатурация: " +(spo2/count).toString(), 1
//                        )
                        file2.appendText(
                            "Therapy " + (LocalDate.now()).toString() + " " + LocalDateTime.now()
                                .format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                ).toString() + " Средняя сатурация: " + (spo2 / count).toString() + " Длительность: " + (elapsedMillis/60000).toInt().toString() + " минут\n")

//                        Storage.instance?.internalStorage?.write(
//                            file2, "Therapy " + (LocalDate.now()).toString() +" "+ LocalDateTime.now().format(
//                                DateTimeFormatter.ofPattern("HH:mm")).toString() + "Средняя сатурация: " +(spo2/count).toString(), 2
//                        )


                        Log.d("FILE", "WRITTEN")
                    }
                    else{
                        Log.d("FILE", "NINONONO")
                    }
                }
                sendInfo(db)
            }

            bluetoothController.closeConnection()
            val intend = Intent(this, StatisticActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }
//        buttonSend.setOnClickListener{
//            bluetoothController.sendMessage("1")
//        }
//
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendInfo(db: DBhelper, ){
        val lasttherdate: String = db.getTherapyInfo(user_login,user_pass)[3]
        if(lasttherdate == (LocalDate.now()).toString()){
            try{
                val inform = db.getTherapyInfo(user_login,user_pass)
                db.addTrerInfo(user_login, (spo2/count).toInt(), 89, inform[2].toInt()+(elapsedMillis/60000).toInt(), (LocalDate.now()).toString())}
            catch (se:ArithmeticException){
                db.addTrerInfo(user_login, 0, 89, (elapsedMillis/60000).toInt(),(LocalDate.now()).toString() )
            }
        }
        else{
            try{
                db.addTrerInfo(user_login, (spo2/count).toInt(), 89, (elapsedMillis/60000).toInt(), (LocalDate.now()).toString())}
            catch (se:ArithmeticException){
                db.addTrerInfo(user_login, 0, 89, (elapsedMillis/60000).toInt(), (LocalDate.now()).toString())
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onReceive(message: String){
        this?.runOnUiThread{
            when(message){
                BluetoothController.BLUETOOTH_CONNECTED -> {
                    Toast.makeText(this, "Oxymeter connected", Toast.LENGTH_SHORT).show()
                    chronometer.start()
                    autocheckbox.isClickable = true
                    val isfilecreated = internalStorageManager!!.createFile(this, ((LocalDate.now()).toString()) + ".txt")
                    if(isfilecreated){
                        Log.d("FILE", "YES")
                        isfile = true
                        file = internalStorageManager!!.getfile(this, ((LocalDate.now()).toString())+".txt")!!
                        Storage.instance?.internalStorage?.write(file,"")
                        file.appendText("Therapy " + (LocalDate.now()).toString() +" "+ LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("HH:mm")).toString() + "\n")


                        Log.d("FILE", "WRITTEN")
                    }
                    else{
                        Log.d("FILE", "NO")
                    }
                }
                BluetoothController.BLUETOOTH_NO_CONNECTED ->{
                    Toast.makeText(this, "Oxymeter disconnected", Toast.LENGTH_SHORT).show()
                }
                else ->{
                    msg = msg + message
                    info = msg.split(",").toMutableList()
                    Log.d("info", info.toString())
                    Log.d("count", info.count().toString())
                    Log.d("msg", msg)
                    if (info.count() == 2) {
                        if (info == mutableListOf("0", "0") || info == mutableListOf(
                                "",
                                "0"
                            ) || info == mutableListOf("00", "0") || info == mutableListOf(
                                "",
                                "00"
                            ) || info == mutableListOf("0", "00") || info == mutableListOf(
                                "0",
                                ""
                            )
                        ) {
                            if (!plusbutton.isClickable) {
                                textSP02.setText("X")
                                textPID.setText("X")
                            }
                        } else {
                            textSP02.setText(info[0])
                            if(autocheckbox.isChecked) {
                                textPID.setText(info[1])
                                litrl = info[1].toDouble()
                            }
                            try {
                                spo2 = spo2 + info[0].toInt()
                            } catch (se: NumberFormatException) {
                            }
                            count = count + 1
                            if(!plusbutton.isClickable) {
                                file.appendText(
                                    LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("HH:mm:ss")
                                    ).toString() + " " + info.toString() + "\n"
                                )
                            }
                            else{
                                info[1] = String.format("%.1f", litr)
                                file.appendText(
                                    LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("HH:mm:ss")
                                    ).toString() + " " + info.toString() + "\n"
                                )
                            }
                            msg = ""
                            info.clear()
                        }
                    } else {
                        if (info.count() == 1) info.clear()
                        else {
                            info.clear()
                            msg = ""
                        }
                    }

                }
            }
        }
    }
    fun initBtAdapter(){
        val bManager = this?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }
    fun initBttAdapter(){
        val bManager = this?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }
    private fun registerBtLauncher(){
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth ON", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Bluetooth OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkBtPermission(): Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        }
        else{
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun checkPermissions(){
        if(!checkBtPermission()){
            registerPermissionListener()
            launchBtPermissions()
        }
    }

    private fun launchBtPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pLauncher.launch(arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        }
        else{
            pLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        }
    }

    private fun registerPermissionListener(){
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

        }
    }

}




