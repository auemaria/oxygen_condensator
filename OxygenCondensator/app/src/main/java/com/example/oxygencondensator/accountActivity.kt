package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class accountActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val db = DBhelper(this,null)
        val user_login = intent.extras?.getString("login").toString()
        val user_pass = intent.extras?.getString("pass").toString()
        val backbutton: Button = findViewById(R.id.backbuttonaccount)

        val fiotext: TextView = findViewById(R.id.fiotext)
        val agetext: TextView = findViewById(R.id.agetext)
        val nameilltext: TextView = findViewById(R.id.nameilltext)

        val inform = db.getAccountInfo(user_login,user_pass)

        fiotext.setText(inform[1])
        agetext.setText(inform[2])
        nameilltext.setText(inform[3])

        backbutton.setOnClickListener(){
            val intend = Intent(this, StatisticActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }
    }
}