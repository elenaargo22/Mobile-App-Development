package com.example.icalculatorwithstaticlayout

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.icalculatorwithstaticlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: Number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[Number::class.java]

        binding.number = viewModel
        binding.lifecycleOwner = this

        setupButtons()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setupButtons() {

        binding.btn0.setOnClickListener { viewModel.onNumberPressed("0") }
        binding.btn1.setOnClickListener { viewModel.onNumberPressed("1") }
        binding.btn2.setOnClickListener { viewModel.onNumberPressed("2") }
        binding.btn3.setOnClickListener { viewModel.onNumberPressed("3") }
        binding.btn4.setOnClickListener { viewModel.onNumberPressed("4") }
        binding.btn5.setOnClickListener { viewModel.onNumberPressed("5") }
        binding.btn6.setOnClickListener { viewModel.onNumberPressed("6") }
        binding.btn7.setOnClickListener { viewModel.onNumberPressed("7") }
        binding.btn8.setOnClickListener { viewModel.onNumberPressed("8") }
        binding.btn9.setOnClickListener { viewModel.onNumberPressed("9") }
        binding.btnDot.setOnClickListener { viewModel.onNumberPressed(".") }


        binding.btnPlus.setOnClickListener { viewModel.onOperationPressed("+") }
        binding.btnMinus.setOnClickListener { viewModel.onOperationPressed("-") }
        binding.btnMult.setOnClickListener { viewModel.onOperationPressed("*") }
        binding.btnDiv.setOnClickListener { viewModel.onOperationPressed("/") }


        binding.btnPlusMinus.setOnClickListener { viewModel.onChangeSign() }

        binding.btnPerc.setOnClickListener { viewModel.onPercentage() }


        binding.btnEqual.setOnClickListener { viewModel.onCalculate() }

        binding.btnClr.setOnClickListener { viewModel.onClear() }

        binding.btnDel.setOnClickListener {
            val current = viewModel.number.value ?: ""
            if (current.isNotEmpty()) {
                viewModel.number.value = current.dropLast(1)
            }
        }
    }
}