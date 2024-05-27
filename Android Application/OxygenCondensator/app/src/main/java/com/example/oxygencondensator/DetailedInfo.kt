package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.time.LocalDate

class DetailedInfo : AppCompatActivity() {
    private val internalStorageManager: FileHelper? = Storage.instance?.internalStorage
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_info)
        val textinfo: TextView = findViewById(R.id.infotext)
        val user_login = intent.extras?.getString("login").toString()
        val user_pass = intent.extras?.getString("pass").toString()
        val buttonshare: Button = findViewById(R.id.buttonshare)
        val buttonback: Button = findViewById(R.id.buttonback)
        val file2 = internalStorageManager!!.getfile(this, "statistic" + user_login.toString() +".txt")
        val data: StringBuilder? = Storage.instance?.internalStorage?.read(file2)
        textinfo.setText(data)
        buttonshare.setOnClickListener(){
            val file = internalStorageManager!!.getfile(this, "statistic" + user_login.toString() +".txt")
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
        buttonback.setOnClickListener(){
            val intend = Intent(this, StatisticActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }
    }
}