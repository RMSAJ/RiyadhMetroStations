package com.bootcamp.stations.homeMap.dataLayer.data

import com.google.android.gms.maps.model.LatLng

data class PlaceResponse(
    val location: GeometryLocation,
    val name: String,
    val vicinity: String,
    val rating: Double
)


data class GeometryLocation(
    val lat: Double,
    val lng: Double
)

fun PlaceResponse.toPlace(): Place = Place(
    name = name,
    latLng = LatLng(location.lat, location.lng),
    address = vicinity,
    rating = rating
)
