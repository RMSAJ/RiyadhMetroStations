package com.bootcamp.stations.homeMap.dataLayer.data

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val id: Int? =-1,
    val location: GeometryLocation=GeometryLocation(),
    val name: String="",
    val vicinity: String="",
    val rating: Double=0.0,
    val line:Line=Line()
)
data class Line(val name:String="",val color: String="#ffffff",val width:Float=10f)

data class GeometryLocation(
    val lat: Double=0.0,
    val lng: Double=0.0
)

data class Markers(
    val markers: MutableList<Place> = mutableListOf()
)



fun PlaceResponse.toPlace(): Place = Place(
    id= id,
    name = name,
    latLng = LatLng(location.lat, location.lng),
    address = vicinity,
    rating = rating,
    line = line
)
