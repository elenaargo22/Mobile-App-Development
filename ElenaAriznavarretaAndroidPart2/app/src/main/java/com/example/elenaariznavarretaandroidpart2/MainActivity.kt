package com.example.elenaariznavarretaandroidpart2

import android.R.attr.tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.elenaariznavarretaandroidpart2.ui.theme.ElenaAriznavarretaAndroidPart2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val ctx = LocalContext.current
            var txt : MutableState<String> = remember {
                mutableStateOf("My old text")
            }
            var shouldTextsBePresented : MutableState<Boolean> = remember {
                mutableStateOf(false)
            }
            var txt2: MutableState<Boolean> = remember{
                mutableStateOf(true)
            }
            var tfValue: MutableState<String> = remember {
                mutableStateOf("")
            }


            Column {
                Button(onClick = {
                    Log.i("myapp", "Entry from my application")
                }) {
                    Text(text = "Save to LogCat")
                }

                Button(onClick = {
                    Toast.makeText(ctx, "Hello There!", LENGTH_LONG).show()
                }) {
                    Text(text = "Show Toast")
                }

                Button(onClick = {
                    if (txt.value == "My old text"){
                        txt.value = "My new text"
                    } else {
                        txt.value = "My old text"
                    }
                    Log.i("myapp", txt.value)

                }) {
                    Text(text = "Change the text")
                }

                Text(
                    text = txt.value, fontSize = 20.sp
                )

                Button(onClick = {
                    shouldTextsBePresented.value = !shouldTextsBePresented.value
                    txt2.value = !txt2.value
                    Log.i("myapp",txt2.value.toString())

                }) {
                    if (txt2.value){
                        Text(text = "Show up colored text")
                    } else{
                        Text(text = "Hide colored texts")
                    }
                }

                if (shouldTextsBePresented.value){
                    MyTexts()
                }

                TextField(value = tfValue.value, onValueChange = {
                    tfValue.value = it
                })
            }

        }
    }

}


@Composable


fun MyTexts() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 0.dp, top = 0.dp, end = 3.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,

        ) {
        Text(
            text = "Hello There!",
            color = Color.Red,
            fontSize = 20.sp,
            letterSpacing = 3.sp
        )
        Text(
            text = "I'm doing so great",
            color = Color.Blue,
            fontSize = 20.sp,
            letterSpacing = 3.sp
        )
        Text(
            text = "And android is so cool",
            color = Color.Magenta,
            fontSize = 20.sp,
            letterSpacing = 3.sp
        )
    }
}