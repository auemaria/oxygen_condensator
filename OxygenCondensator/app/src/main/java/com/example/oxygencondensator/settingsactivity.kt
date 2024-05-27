package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class settingsactivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingsactivity)
        val user_login = intent.extras?.getString("login").toString()
        var user_pass = intent.extras?.getString("pass").toString()
        val accountname: TextView = findViewById(R.id.accountnametextset)
        val wrongpass: TextView = findViewById(R.id.wrongpass)
        val oldPass: EditText = findViewById(R.id.oldpasswordedit)
        val emailtext: TextView = findViewById(R.id.emailtext)
        val newPass: EditText = findViewById(R.id.newpasswordedit)
        val changebutton: Button = findViewById(R.id.changebutton)
        val backbutton: Button = findViewById(R.id.backbutton)
        val accountexitbut: Button = findViewById(R.id.accountexit)

        val db = DBhelper(this,null)
        val inform = db.getAccountInfo(user_login,user_pass)
        val email = inform[0]

        accountname.setText(user_login)
        emailtext.setText(email)

        changebutton.setOnClickListener{
            val old_pass = oldPass.text.toString().trim()
            val new_pass = newPass.text.toString().trim()
            if(old_pass==""||new_pass=="")
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show()
            else {
                if (old_pass == user_pass) {
                    if (user_pass == new_pass){
                        wrongpass.setText("")
                        newPass.text.clear()
                        Toast.makeText(this, "Введены одинаковые пароли!", Toast.LENGTH_LONG).show()

                    }
                    else{
                        wrongpass.setText("")
                        db.changepass(user_login, new_pass)
                        Toast.makeText(this, "Смена прошла успешно", Toast.LENGTH_LONG).show()
                        user_pass = new_pass
                        oldPass.text.clear()
                        newPass.text.clear()
                    }

                } else {
                    wrongpass.setText("Неверный пароль")
                }
            }
            }
        backbutton.setOnClickListener{
            val intend = Intent(this, StatisticActivity::class.java)
            intend.putExtra("login", user_login)
            intend.putExtra("pass", user_pass)
            startActivity(intend)
        }
        accountexitbut.setOnClickListener{
            val intend = Intent(this, AusActivity::class.java)
            startActivity(intend)
        }
    }
}