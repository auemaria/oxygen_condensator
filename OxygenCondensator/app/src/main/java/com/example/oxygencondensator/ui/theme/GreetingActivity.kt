package com.example.oxygencondensator.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.viewpager.widget.PagerAdapter
import com.example.oxygencondensator.CondensatorActivity
import com.example.oxygencondensator.R
import com.example.oxygencondensator.StatisticActivity


class GreetingActivity : ComponentActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        var count = 0
        val buttonNext: Button = findViewById(R.id.button_welcome)
        val welcomeText: TextView = findViewById(R.id.text_welcome)
        val descText: TextView = findViewById(R.id.text_desc_welcome)
        val welcomePhoto: View = findViewById(R.id.ph_welcome)
        val icPhoto: ImageView = findViewById(R.id.caroisel_welcome)
        val ph_carousel = listOf(R.drawable.ph_welcome_2, R.drawable.ph_welcome_3, R.drawable.ph_welcome_4)
        val ic_carousel = listOf(R.drawable.welcome_carousel_2, R.drawable.welcome_carousel_3, R.drawable.welcome_carousel_4)
        buttonNext.setOnClickListener {
//            welcomePhoto.setImageResource(ph_carousel.get(count))
            icPhoto.setImageResource(ic_carousel.get(count))

            if (count==1){
                val intend = Intent(this, StatisticActivity::class.java)
                startActivity(intend)
            }
            count += 1
        }
        }
}

