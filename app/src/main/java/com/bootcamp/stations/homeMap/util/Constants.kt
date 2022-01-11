package com.bootcamp.stations.homeMap.util

import com.bootcamp.stations.homeMap.ui.HomeFragment
import com.google.android.gms.maps.model.LatLng

object Constants {








}

     val TAG = HomeFragment::class.java.simpleName
     const val DEFAULT_ZOOM = 15
     const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
     const val KEY_CAMERA_POSITION = "camera_position"
     const val KEY_LOCATION = "location"
        val defaultLocation = LatLng(24.582133783959872, 46.76407041289462)

