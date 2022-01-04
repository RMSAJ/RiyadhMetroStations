package com.bootcamp.stations.homeMap.ui

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.stations.homeMap.dataLayer.data.Line
import com.bootcamp.stations.homeMap.dataLayer.data.PlacesReader
import com.bootcamp.stations.homeMap.util.trainIcon
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place

class MapViewModel:ViewModel() {

    //    private lateinit var auth: FirebaseAuth
private val _listOfMarkers = MutableLiveData<List<Place>>()
    val listOfMarkers get() = _listOfMarkers

    private fun addPolyLine(googleMap: GoogleMap,  ) {
        for (line in places) {
            addMarkers(googleMap, line.value)
            val polyLineOption = addNewPolyline(line.value.map {
                LatLng(it.latLng.latitude, it.latLng.longitude)
            }, line.key.width, Color.parseColor(line.key.color))
            googleMap.addPolyline(polyLineOption).tag = line.key.name
        }
        //region poly1
        //endregion
    }
    private fun places(context: Context) {
        val places: Map<Line, List<com.bootcamp.stations.homeMap.dataLayer.data.Place>> by lazy {
            PlacesReader(context).read()
        }
    }

    private fun addNewPolyline(
        listOfPoint: List<LatLng>,
        width: Float,
        color: Int
    ): PolylineOptions {
        return PolylineOptions()
            .clickable(true)
            .addAll(listOfPoint)
            .width(width)
            .color(color)
    }

    //region  [add markers to map and call in onMapCreated]
    private fun addMarkers(googleMap: GoogleMap, listOfPoint: List<Place>, context: Context) {
        listOfPoint.forEach { place ->
            if (place.name != null){
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .title(place.name)
                        .position(LatLng(place.latLng!!.latitude, place.latLng!!.longitude))
                        .icon(trainIcon(context))
                )
            }
        }
    }
}