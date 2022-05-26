package com.rgonslayer.tiktokyc.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binds activity layout
        setContentView(R.layout.activity_main)

        weatherTask().execute()

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "SG 2022 Youth Camp Android"

        val btnBootcamp = findViewById<Button>(R.id.btn_bootcamp)

        btnBootcamp.setOnClickListener {
            Toast.makeText(
                this,
                "Hello World!",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
}