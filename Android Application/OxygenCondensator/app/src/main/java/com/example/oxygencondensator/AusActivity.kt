package com.example.oxygencondensator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.oxygencondensator.ui.theme.GreetingActivity

class AusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aus)

        val linkToReg: TextView = findViewById(R.id.linktoreg)
        val userLogin: EditText = findViewById(R.id.login_aus)
        val userPass: EditText = findViewById(R.id.pass_aus)
        val buttonAus: Button = findViewById(R.id.button_aus)

        linkToReg.setOnClickListener{
            val intend = Intent(this, MainActivity::class.java)
            startActivity(intend)
        }
        buttonAus.setOnClickListener{
            val user_login = userLogin.text.toString().trim()
            val user_pass = userPass.text.toString().trim()

            if(user_login==""||user_pass=="")
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show()
            else{
                val db = DBhelper(this,null)
                val isAuth = db.getUser(user_login,user_pass)

                if(isAuth){
                    userLogin.text.clear()
                    userPass.text.clear()
                    val account_information = db.getAccountInfo(user_login,user_pass)
                    if (account_information[1]!=""){
                        val intend = Intent(this, StatisticActivity::class.java)
                        intend.putExtra("login", user_login)
                        intend.putExtra("pass", user_pass)
                        startActivity(intend)
                    }
                    else{
                        val intend = Intent(this, SurveyActivity::class.java)
                        intend.putExtra("login", user_login)
                        intend.putExtra("pass", user_pass)
                        startActivity(intend)
                    }
                }
                else{
                    Toast.makeText(this, "Пользователь $user_login НЕ авторизирован", Toast.LENGTH_LONG).show()

                }

            }
        }
    }
}