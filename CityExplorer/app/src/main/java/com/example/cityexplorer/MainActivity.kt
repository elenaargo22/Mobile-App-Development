package com.example.cityexplorer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.draw.clip


data class Challenge(val title: String, val category: String, val targetPlace: Place, val distance: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

enum class Screen { MAP, HISTORY, STATISTICS }

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.MAP) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Map") },
                    label = { Text("Explore") },
                    selected = currentScreen == Screen.MAP,
                    onClick = { currentScreen = Screen.MAP }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text("History") },
                    selected = currentScreen == Screen.HISTORY,
                    onClick = { currentScreen = Screen.HISTORY }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Stats") },
                    label = { Text("Stats") },
                    selected = currentScreen == Screen.STATISTICS,
                    onClick = { currentScreen = Screen.STATISTICS }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.MAP -> MainScreen()
                Screen.HISTORY -> HistoryScreen(context = context)
                Screen.STATISTICS -> StatisticsScreen(context = context)

            }
        }
    }
}

@Composable
fun StatisticsScreen(context: Context) {
    val history = HistoryManager.getHistory(context)

    val totalChallenges = history.size
    val totalDistance = history.sumOf { it.distance }
    val avgDistance = if (totalChallenges > 0) totalDistance / totalChallenges else 0
    val topCategory = history.groupingBy { it.category }.eachCount().maxByOrNull { it.value }?.key ?: "N/A"

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text("Your Explorer Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Challenges: $totalChallenges", style = MaterialTheme.typography.titleMedium)
                Text("Total Distance: $totalDistance m", style = MaterialTheme.typography.titleMedium)
                Text("Average Distance: $avgDistance m/challenge", style = MaterialTheme.typography.titleMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Favorite Category: ${topCategory.uppercase()}", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var activeChallenge by remember { mutableStateOf<Challenge?>(null) }
    var buttonLabel by remember { mutableStateOf("Generate Challenge") }
    var isLoading by remember { mutableStateOf(false) }

    var userLat by remember { mutableStateOf(50.0663) }
    var userLon by remember { mutableStateOf(19.9137) }
    var locationPermissionGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        locationPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionGranted) permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text(text = "City Explorer Challenge", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                buttonLabel = "Searching..."
                if (locationPermissionGranted) {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token).addOnSuccessListener { location ->
                        if (location != null) { userLat = location.latitude; userLon = location.longitude }
                        fetchPlacesWithLocation(userLat, userLon, context) { res, label -> activeChallenge = res; buttonLabel = label; isLoading = false }
                    }
                } else {
                    fetchPlacesWithLocation(userLat, userLon, context) { res, label -> activeChallenge = res; buttonLabel = label; isLoading = false }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(buttonLabel) }

        activeChallenge?.let { challenge ->
            Spacer(modifier = Modifier.height(16.dp))

            Text("Target: ${challenge.targetPlace.name}", style = MaterialTheme.typography.titleLarge)
            Text("Category: ${challenge.category.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.bodyMedium)
            Text("Distance: ${challenge.distance} m", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    HistoryManager.saveChallenge(context, challenge)
                    activeChallenge = null
                    buttonLabel = "Generate Challenge"
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Mark as Completed") }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false) // Peso flexible pero no agresivo
                    .heightIn(max = 300.dp)   // Altura máxima absoluta
                    .clip(RoundedCornerShape(12.dp)) // Recorte visual
            ) {
                OSMMap(
                    places = listOf(challenge.targetPlace),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun OSMMap(places: List<Place>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(modifier = modifier, factory = {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
        }
    }, update = { mapView ->
        mapView.overlays.clear()
        places.forEach { place ->
            val geoPoint = GeoPoint(place.latitude, place.longitude)
            val marker = Marker(mapView).apply { position = geoPoint; title = place.name }
            mapView.overlays.add(marker)
            mapView.controller.animateTo(geoPoint)
        }
        mapView.invalidate()
    })
}

fun fetchPlacesWithLocation(
    lat: Double,
    lon: Double,
    context: Context,
    onResult: (Challenge?, String) -> Unit
) {
    val userLocation = org.osmdroid.util.GeoPoint(lat, lon)
    val category = ChallengeEngine.getNextCategory(context)

    Thread {
        try {
            val client = OkHttpClient()
            val query = "[out:json];node(around:1000,$lat,$lon)[amenity=$category];out 5;".let { java.net.URLEncoder.encode(it, "UTF-8") }
            val url = "https://overpass-api.de/api/interpreter?data=$query"

            val request = Request.Builder().url(url).addHeader("User-Agent", "App/1.0").build()
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: "{}")
            val elements = json.optJSONArray("elements")
            val places = mutableListOf<Place>()

            elements?.let {
                for (i in 0 until it.length()) {
                    val item = it.getJSONObject(i)
                    val latPlace = item.getDouble("lat")
                    val lonPlace = item.getDouble("lon")
                    val name = item.optJSONObject("tags")?.optString("name") ?: "Unknown Place"

                    places.add(Place(name, "Category: $category", latPlace, lonPlace))
                }
            }

            (context as android.app.Activity).runOnUiThread {
                val best = ChallengeEngine.selectBestPlace(places, lat, lon, context)
                if (best != null) {
                    // Cálculo real de distancia
                    val targetLoc = org.osmdroid.util.GeoPoint(best.latitude, best.longitude)
                    val distance = userLocation.distanceToAsDouble(targetLoc).toInt()

                    val challenge = Challenge("Visit a $category", category, best, distance)
                    onResult(challenge, "Challenge Active")
                } else {
                    onResult(null, "No places found")
                }
            }
        } catch (e: Exception) {
            onResult(null, "Error: ${e.message}")
        }
    }.start()
}