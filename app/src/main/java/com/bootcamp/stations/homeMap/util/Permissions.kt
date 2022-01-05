package com.bootcamp.stations.homeMap.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentProvider
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bootcamp.stations.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient


fun getDeviceLocation(context: FragmentActivity,locationPermissionGranted:Boolean,
                       googleMap: GoogleMap, lastKnownLocation: Location,
                       fusedLocationProviderClient: FusedLocationProviderClient
 ) {

    try {
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(context) {
                if (it.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation.set( it.result)
                    if (lastKnownLocation != null) {
                        googleMap.isMyLocationEnabled = true
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation.latitude,
                                    lastKnownLocation.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                    }
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.")
                    Log.e(TAG, "Exception: %s", it.exception)
                    googleMap.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                    googleMap.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        }
    } catch (e: SecurityException) {
        Log.e("Exception: %s", e.message, e)
    }
}

@SuppressLint("MissingPermission")
fun showCurrentPlace(googleMap: GoogleMap, permission: Boolean, placesClient:PlacesClient, context: Context) {
    if (googleMap == null) {
        return
    }
    if (permission) {
        // Use fields to define the data types to return.
        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        // Use the builder to create a FindCurrentPlaceRequest.
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        val placeResult = placesClient.findCurrentPlace(request)
        placeResult.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
//                    val likelyPlaces = task.result
                googleMap.isMyLocationEnabled = true

            } else {
                Log.e(TAG, "Exception: %s", task.exception)
            }
        }
    } else {
        defaultMarkerOfTheUSer(googleMap,context )
    }
}

private fun defaultMarkerOfTheUSer(googleMap: GoogleMap,context: Context ) {
    // The user has not granted permission.
    Log.i(TAG, "The user did not grant location permission.")
    googleMap.addMarker(
        MarkerOptions()
            .title((R.string.default_info_title.toString()))
            .icon(Constants.personIcon(context))
            .position(defaultLocation)
            .snippet((R.string.default_info_snippet.toString()))
    )

}

