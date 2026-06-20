package com.example.lab12

import android.content.Context
import android.media.VolumeShaper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lab12.ui.theme.Lab12Theme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.Layout
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OSMMap()
        }
    }
}

private fun createPOIMarker(place: Place, mapView: MapView){
    val marker = Marker(mapView)
    marker.position = GeoPoint(
        place.latitude,
        place.longitude
    )
    marker.title = place.name
    mapView.overlays.add(marker)
}

private fun createPOIMarkerWithToast(place: Place, mapView: MapView){
    val marker = Marker(mapView)
    marker.position = GeoPoint(
        place.latitude, place.longitude
    )
    marker.title = place.name

    marker.setOnMarkerClickListener { _, _ ->
        Toast.makeText(
            mapView.context,
            "${place.name} - ${place.description}",
            Toast.LENGTH_SHORT
        ).show()
        true
    }
    mapView.overlays.add(marker)
}

private fun definePOIPlaces(): List<Place> {
    val places = listOf(
        Place(
            "AGH University",
            "Faculty of Space Technologies",
            50.0663,
            19.9137
        ),
        Place(
            "Zamek Wawel",
            "The Castle of Krakow",
            50.053851,
            19.935848
        ),
        Place(
            "Rynek Glowny",
            "The main square",
            50.062123,
            19.937173
        )
    )
    return places
}
@Composable
fun OSMMap() {
    val places = definePOIPlaces()
    val context = LocalContext.current

    var selectedPlaceByClick by remember { mutableStateOf<Place?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                buildMVConfiguration(context)
                createMapView(context, places, onPlaceClick = { place ->
                    selectedPlaceByClick = place
                })
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.9f))
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = if (selectedPlaceByClick != null) {
                    "${selectedPlaceByClick!!.name} - ${selectedPlaceByClick!!.description}"
                } else {
                    "No place selected"
                },
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}
private fun buildMVConfiguration(context: Context) {
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences(
            "osmdroid",
            Context.MODE_PRIVATE
        )
    )
    Configuration.getInstance().userAgentValue = context.packageName
}
private fun createPOIMarkerWithUIUpdate(
    place: Place,
    mapView: MapView,
    onPlaceClick: (Place) -> Unit
) {
    val marker = Marker(mapView)
    marker.position = GeoPoint(place.latitude, place.longitude)
    marker.title = place.name
    marker.snippet = place.description
    marker.setOnMarkerClickListener { clickedMarker, _ ->
        onPlaceClick(place)
        clickedMarker.showInfoWindow()
        true
    }

    mapView.overlays.add(marker)
}
private fun createMapView(
    context: Context,
    places: List<Place>,
    onPlaceClick: (Place) -> Unit
): MapView {
    return MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        controller.setZoom(15.0)

        controller.setCenter(GeoPoint(50.062123, 19.937173))

        places.forEach { place ->
            createPOIMarkerWithUIUpdate(place, mapView = this, onPlaceClick)
        }

        var customMarkerCount = 1

        val mReceive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    val newMarker = Marker(this@apply)
                    newMarker.position = p
                    newMarker.title = "Custom Marker $customMarkerCount"
                    newMarker.snippet = "Lat: ${p.latitude}, Lon: ${p.longitude}"

                    overlays.add(newMarker)
                    customMarkerCount++
                    invalidate()
                    newMarker.showInfoWindow()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(mReceive)
        overlays.add(overlayEvents)
    }
}
