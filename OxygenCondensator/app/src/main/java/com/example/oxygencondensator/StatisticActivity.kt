package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toUpperCase
import androidx.core.content.FileProvider
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class StatisticActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    private val internalStorageManager: FileHelper? = Storage.instance?.internalStorage
    @SuppressLint("WrongViewCast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic3)
        val db = DBhelper(this,null)
        val user_login = intent.extras?.getString("login").toString()
        val user_pass = intent.extras?.getString("pass").toString()
        val inform = db.getTherapyInfo(user_login,user_pass)
        val buttonStart: Button = findViewById(R.id.startbutton)
        val buttoninfo:Button = findViewById(R.id.buttoninfo)
        val settinhsbutton: ImageButton = findViewById(R.id.settingsbutton)
        val accountbutton: ImageButton = findViewById(R.id.accountbutton)
        val shareButton: Button = findViewById(R.id.sharefile)
        val accountshort: TextView = findViewById(R.id.accountnametext)
        val averagesat: TextView = findViewById(R.id.averagesaturationtext)
        val averagepulse: TextView = findViewById(R.id.averagepulsetext)
        var file: File
        var isfile: Boolean = false
        val todayther: TextView = findViewById(R.id.todaytherdur)


        if (db.getTherapyInfo(user_login,user_pass)[3] == (LocalDate.now()).toString()){
            averagesat.setText(inform[0])
            averagepulse.setText(inform[1])
            todayther.setText(inform[2])
        }
        else{
            averagesat.setText("0")
            averagepulse.setText("0")
            todayther.setText("0")
        }


        accountshort.setText((user_login[0].toString()).uppercase(Locale.ROOT))
        buttonStart.setOnClickListener{
            val intend = Intent(this, CondensatorActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }
        shareButton.setOnClickListener(){
            val file = internalStorageManager!!.getfile(this, ((LocalDate.now()).toString())+".txt")
            if (file != null) {
                if (file.exists()) {
                    val intent = Intent(Intent.ACTION_SEND)
                    val uri =
                        FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setType("*/*")
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Nothing to share!", Toast.LENGTH_SHORT).show()
                }
                }
            }
        buttoninfo.setOnClickListener(){
            val file2 = internalStorageManager!!.getfile(this, "statistic" + user_login.toString() +".txt")
            if (file2 != null) {
                if (file2.exists()) {
                    val intend = Intent(this, DetailedInfo::class.java)
                    intend.putExtra("login", user_login)
                    intend.putExtra("pass", user_pass)
                    startActivity(intend)
                }
                else{
                    Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        settinhsbutton.setOnClickListener(){
            val intend = Intent(this, settingsactivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }

        accountbutton.setOnClickListener(){
            val intend = Intent(this, accountActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }

    }
}