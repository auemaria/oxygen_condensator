package com.example.oxygencondensator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.oxygencondensator.ui.theme.GreetingActivity

class SurveyActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        val nameQuiz: EditText = findViewById(R.id.name_survey)
        val ageQuiz: EditText = findViewById(R.id.age_survey)
        val illQuiz: EditText = findViewById(R.id.illness_survey)
        val buttonQuiz: Button = findViewById(R.id.survey_button)

        buttonQuiz.setOnClickListener{
            val user_age = ageQuiz.text.toString().trim()
            val user_name = nameQuiz.text.toString().trim()
            val user_ill = illQuiz.text.toString().trim()

            if (user_age == "" || user_name == "" || user_ill == "")
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show()
            else{
                if(user_age=="0"){
                    Toast.makeText(this, "Введите корректный возраст!", Toast.LENGTH_LONG).show()
                    ageQuiz.text.clear()
                }
                else {
                    val db = DBhelper(this, null)
                    val user_login = intent.extras?.getString("login").toString()
                    val user_pass = intent.extras?.getString("pass").toString()
                    db.addInfo(user_login, user_name, user_age.toInt(), user_ill)
                    Toast.makeText(
                        this,
                        "Информация пользователя $user_login добавлена",
                        Toast.LENGTH_LONG
                    ).show()
                    val intend = Intent(this, StatisticActivity::class.java)
                    intend.putExtra("login", user_login)
                    intend.putExtra("pass", user_pass)
                    startActivity(intend)
                }
            }
        }

    }
}