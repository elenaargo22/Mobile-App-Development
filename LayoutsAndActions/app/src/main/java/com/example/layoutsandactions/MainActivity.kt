package com.example.layoutsandactions

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.layoutsandactions.databinding.ActivityActionsBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActionsBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityActionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        user = ViewModelProvider(this)[User::class.java]
        user.firstName.value = "Alexander"
        user.lastName.value = "TheGreat"

        binding.user = user
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main4)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupButtonListener()
        serShowToastButtonListener()
        setWriteToTextViewButtonListener()
        setUpdateUserDataButtonListener()
    }

    fun writeToLogCat(view: View) {
        Log.i("MyApp", "Message from my app")
    }


    private fun setupButtonListener(){
        binding.btnWriteToLogcat.setOnClickListener {
            Log.i("MyAppList","Message From The BindListener")
        }
    }

    private fun serShowToastButtonListener(){
        binding.btnShowToast.setOnClickListener {
            Toast.makeText(
                this, "Message From My App", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setWriteToTextViewButtonListener(){
        binding.btnWriteToTextview.setOnClickListener {
            binding.tvWriteSomething.text = "I love this game"
        }
    }

    private fun setUpdateUserDataButtonListener() {
        binding.btnWriteUser.setOnClickListener {
            Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show()
        }
    }
}