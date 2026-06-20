package com.example.lab11

import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab11.ui.theme.Lab11Theme
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column() {
                Button(
                    onClick = {
                        Log.d("LAB11", "Button clicked")
                        downloadPost()
                    }
                ) {
                    Text("Load data")
                }
                var text by remember { mutableStateOf("No data") }
                Button(
                    onClick = {
                        Thread{
                            val downloadedText = downloadPostForUI()

                            runOnUiThread {
                                text = downloadedText
                            }
                        }.start()
                    }
                ) {
                    Text("Load data to UI")
                }
                Text(text)

                Button(
                    onClick = {
                        Log.d("LAB11", "Third Button clicked")
                        downloadPost()
                    }
                ) {
                    Text("Load many posts to UI")
                }

                var titles by remember {
                    mutableStateOf(listOf<String>())
                }

                Button(
                    onClick = {
                        Log.d("LAB11", "Fourth Button clicked")
                        Thread{
                            val downloadedTitles = downloadPostTitles()
                            runOnUiThread {
                                titles = downloadedTitles
                            }
                        }.start()
                    }
                ) {
                    Text("Load many posts to UI")
                }

                var postsList by remember { mutableStateOf(listOf<Post>()) }
                var selectedPostComments by remember { mutableStateOf(listOf<Comment>()) }
                var selectedPostId by remember { mutableStateOf<Int?>(null) }

                Button(
                    onClick = {
                        Log.d("LAB11", "Fifth Button Clicked")
                        Thread{
                            val downloadedPosts = downloadAllAvailablePost()
                            runOnUiThread {
                                postsList = downloadedPosts
                            }
                        }.start()
                    }
                ) {
                    Text("Load Posts Titles as Cards")
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(postsList) { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    // Al hacer clic en la tarjeta
                                    selectedPostId = post.id
                                    Thread {
                                        // Descargamos los comentarios correspondientes de internet
                                        val comments = downloadCommentsForPost(post.id)
                                        runOnUiThread {
                                            selectedPostComments = comments
                                        }
                                    }.start()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFEFEFEF) // Color gris tenue como el PDF [cite: 328]
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Post #${post.id}", style = MaterialTheme.typography.labelLarge)
                                Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = post.body, style = MaterialTheme.typography.bodySmall)

                                // SI ESTE POST FUE SELECCIONADO, RENDERIZAR SUS COMENTARIOS
                                if (selectedPostId == post.id) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Comments for: ${post.title.take(25)}...",
                                        color = Color.DarkGray,
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                    // Iteramos e inyectamos los comentarios
                                    selectedPostComments.forEach { comment ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text(text = comment.email, color = Color.Blue, style = MaterialTheme.typography.bodySmall)
                                                Text(text = comment.body, style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

fun downloadAllAvailablePost(): List<Post> {
    try {
        val client = OkHttpClient()
        val request = Request.Builder().url("https://jsonplaceholder.typicode.com/posts").build()
        val response = client.newCall(request).execute()
        val result = response.body?.string() ?: "[]"

        val jsonArray = JSONArray(result)
        val posts = mutableListOf<Post>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)

            val post = Post(
                id = item.getInt("id"),
                title = item.getString("title"),
                body = item.getString("body")
            )
            posts.add(post)
        }

        return posts

    } catch (e: Exception) {
        return emptyList()
    }
}
fun downloadCommentsForPost(postId: Int): List<Comment> {
    return try {
        val client = OkHttpClient()
        // El endpoint requiere el ID del post en la URL
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts/$postId/comments")
            .build()
        val response = client.newCall(request).execute()
        val result = response.body?.string() ?: "[]"

        val jsonArray = JSONArray(result)
        val comments = mutableListOf<Comment>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            comments.add(
                Comment(
                    postId = item.getInt("postId"),
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    email = item.getString("email"),
                    body = item.getString("body")
                )
            )
        }
        comments
    } catch (e: Exception) {
        emptyList()
    }
}
fun downloadPostTitles(): List<String>{
    return try {
        val client = OkHttpClient()
        val request = Request.Builder().url("https://jsonplaceholder.typicode.com/posts").build()
        val response = client.newCall(request).execute()
        val result = response.body?.string()?: "[]"
        val jsonArray = JSONArray(result)

        val titles = mutableListOf<String>()

        for (i in 0 until jsonArray.length()){
            val item = jsonArray.getJSONObject(i)
            val title = item.getString("title")
            titles.add(title)
        }
        titles
    } catch (e: Exception){
        listOf("Error: ${e.message}")
    }
}

fun downloadAllAvailablePosts(): String{
    try{
        val client = OkHttpClient()
        val request = Request.Builder().url("https://jsonplaceholder.typicode.com/posts").build()
        val response = client.newCall(request).execute()
        val result = response.body?.string()?: "[]"

        val jsonArray = JSONArray(result)
        val posts = mutableListOf<Post>()

        for (i in 0 until jsonArray.length()){
            val item = jsonArray.getJSONObject(i)

            val post = Post(
                id = item.getInt("id"),
                title = item.getString("title"),
                body = item.getString("body")
            )
            posts.add(post)
        }
        return result
    } catch (e: Exception){
       return "Error: ${e.message}"
    }
}
fun downloadPostForUI(): String{
    try {
        val client = OkHttpClient()

        val request = Request.Builder().url("https://jsonplaceholder.typicode.com/posts/1").build()

        val response = client.newCall(request).execute()

        return response.body?.string()
            ?: "No data"
    } catch (e: Exception){
        return "Error: ${e.message}"
    }
}
fun downloadPost(){
    Thread{
        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(
                "https://jsonplaceholder.typicode.com/posts/1"
            ).build()
            val response = client.newCall(request).execute()
            val result = response.body?.string()
            Log.d("LAB11",result.toString())

            val json = JSONObject(result.toString())
            val title = json.getString("title")

            Log.d("LAB11",title)

            val post = Post(
                id = json.getInt("id"),
                title = json.getString("title"),
                body = json.getString("body")
            )

            Log.d("LAB11", post.toString())
        }catch (e: Exception){
            Log.d("LAB11", e.message.toString())
        }
    }.start()
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab11Theme {
        Greeting("Android")
    }
}