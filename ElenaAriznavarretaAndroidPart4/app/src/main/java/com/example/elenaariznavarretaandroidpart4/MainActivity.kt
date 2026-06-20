package com.example.elenaariznavarretaandroidpart4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.elenaariznavarretaandroidpart4.ui.theme.ElenaAriznavarretaAndroidPart4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElenaAriznavarretaAndroidPart4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BmiCalculator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BmiCalculator(modifier: Modifier = Modifier) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Float?>(null) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val w = weight.toFloatOrNull()
                val h = height.toFloatOrNull()

                if (w == null || h == null) {
                    Toast.makeText(context, "Provide valid data", Toast.LENGTH_SHORT).show()
                } else if (h <= 0) {
                    Toast.makeText(context, "Height must be > 0", Toast.LENGTH_SHORT).show()
                } else {
                    bmiResult = w / (h * h)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate BMI")
        }

        bmiResult?.let { bmi ->
            val diagnosis = getDiagnosis(bmi)
            val imageRes = getDiagnosisImage(bmi)
            Text(
                text = "BMI: %.2f\nDiagnosis: %s".format(bmi, diagnosis),
                style = MaterialTheme.typography.headlineSmall
            )
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = diagnosis,
                modifier = Modifier.size(200.dp).padding(top = 16.dp)
            )
        }
    }
}

fun getDiagnosis(bmi: Float): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9 -> "Healthy"
        bmi in 25.0..29.9 -> "Overweight"
        bmi in 30.0..34.9 -> "Obesity"
        else -> "High Obesity"
    }
}

fun getDiagnosisImage(bmi: Float): Int{
    return when{
        bmi < 18.5 -> R.drawable.ic_underweight
        bmi in 18.5..24.9 -> R.drawable.ic_healthy
        bmi in 25.0..29.9 -> R.drawable.ic_overweight
        bmi in 30.0..34.9 -> R.drawable.ic_obesity
        else -> R.drawable.ic_highobesity
    }
}
