package com.bootcamp.stations.homeMap.dataLayer.data

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val id: Int? = null,
    val location: GeometryLocation? = null,
    val name: String? = null,
    val vicinity: String? = null,
    val rating: Double? = null,
    val line:Line?=Line()
)
data class Line(val name:String="",val color: String="#ffffff",val width:Float=10f)

data class GeometryLocation(
    val lat: Double,
    val lng: Double
)

data class Markers(
    val markers: MutableList<Place> = mutableListOf()
)



fun PlaceResponse.toPlace(): Place = Place(
    id= id,
    name = name,
    latLng = LatLng(location!!.lat, location.lng),
    address = vicinity,
    rating = rating,
    line = line?:Line()
)
