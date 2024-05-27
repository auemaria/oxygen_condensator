package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.oxygencondensator.ui.theme.OxygenCondensatorTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPass: EditText = findViewById(R.id.user_pass)
        val userEmail: EditText = findViewById(R.id.user_email)
        val buttonReg: Button = findViewById(R.id.button)
        val linkToAus: TextView = findViewById(R.id.linktoaus)

        buttonReg.setOnClickListener{
            val user_login = userLogin.text.toString().trim()
            val user_pass = userPass.text.toString().trim()
            val user_email = userEmail.text.toString().trim()

            if(user_login==""||user_pass==""||user_email=="")
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show()
            else{
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    val user = User(user_login, user_pass, user_email)

                    val db = DBhelper(this, null)
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь $user_login добавлен", Toast.LENGTH_LONG)
                        .show()

                    userLogin.text.clear()
                    userPass.text.clear()
                    userEmail.text.clear()

                    val intend = Intent(this, AusActivity::class.java)
                    startActivity(intend)
                }
                else{
                    Toast.makeText(this, "Введите корректную почту!", Toast.LENGTH_LONG).show()
                }
            }
        }

        linkToAus.setOnClickListener{
            val intend = Intent(this, AusActivity::class.java)
            startActivity(intend)
        }


    }
}
