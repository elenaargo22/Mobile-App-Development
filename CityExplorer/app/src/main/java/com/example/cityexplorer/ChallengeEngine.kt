package com.example.cityexplorer

import android.content.Context
import org.osmdroid.util.GeoPoint

object ChallengeEngine {

    fun getNextCategory(context: Context): String {
        val history = HistoryManager.getHistory(context)
        val allCategories = listOf("cafe", "restaurant", "library", "cinema", "pub")

        if (history.isEmpty()) return allCategories.random()

        val lastCategory = history.first().category

        val availableCategories = allCategories.filter { it != lastCategory }

        return availableCategories.random()
    }

    fun selectBestPlace(places: List<Place>, userLat: Double, userLon: Double, context: Context): Place? {
        val history = HistoryManager.getHistory(context)
        val userLocation = GeoPoint(userLat, userLon)

        val validPlaces = places.filter { place ->
            val placeLocation = GeoPoint(place.latitude, place.longitude)
            val distance = userLocation.distanceToAsDouble(placeLocation)

            val isNotVisited = history.none { completed -> completed.targetPlace.name == place.name }

            val isFarEnough = distance > 100

            val isNotTooFar = distance < 3000

            isNotVisited && isFarEnough && isNotTooFar
        }

        return validPlaces.randomOrNull()
    }
}