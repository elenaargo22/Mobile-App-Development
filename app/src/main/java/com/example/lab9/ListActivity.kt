package com.example.lab9

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab9.databinding.ActivityListBinding
import com.example.lab9.databinding.ActivityMainBinding

class ListActivity : AppCompatActivity() {
    private val flowerNames = arrayListOf(
        "Rose",
        "Tulip",
        "Daisy"
    )

    private val flowerImages = arrayListOf(
        R.drawable.rosa,
        R.drawable.tulip,
        R.drawable.daisy
    )

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initFlowerList()
        var flowerAdapter = FlowerNameAdapter(this,flowerNames, flowerImages)
        binding.flowerList.adapter = flowerAdapter
    }

//    private fun initFlowerList(){
//        listOfFlowers.add("Rose")
//        listOfFlowers.add("Tulip")
//        listOfFlowers.add("Daisy")
//
//        flowerImages.add(R.drawable.rose)
//        flowerImages.add(R.drawable.tulip)
//        flowerImages.add(R.drawable.daisy)
//    }
}