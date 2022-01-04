package com.bootcamp.stations.homeMap.dataLayer.data

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val location: GeometryLocation,
    val name: String,
    val vicinity: String,
    val rating: Double,
    val line:Line?=Line()
)
data class Line(val name:String="",val color: String="#ffffff",val width:Float=10f)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
)

fun PlaceResponse.toPlace(): Place = Place(
    name = name,
    latLng = LatLng(location.lat, location.lng),
    address = vicinity,
    rating = rating,
    line = line?:Line()
)
