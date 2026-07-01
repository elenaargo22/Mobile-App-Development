package com.example.lab8

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab8.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWriteToLogcat.setOnClickListener {
            Log.d("LAB8_DEBUG", "Message sended to LogCat")
        }

        binding.btnShowToast.setOnClickListener {
            Toast.makeText(this, "Hello! This is a toast message", Toast.LENGTH_SHORT).show()
        }

        binding.btnWriteToEdittext.setOnClickListener {
            binding.etSimpleText.setText("Text inserted")
        }

        binding.btnBackToFirst.setOnClickListener {
            finish()
        }
    }
}