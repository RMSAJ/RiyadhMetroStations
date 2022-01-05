package com.bootcamp.stations.homeMap.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.bootcamp.stations.R
import com.bootcamp.stations.homeMap.HomeFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

object Constants {
    fun trainIcon(context: Context): BitmapDescriptor {
        val trainIcon: BitmapDescriptor by lazy {
            val color = ContextCompat.getColor(context, R.color.Primary_Green_900)
            BitmapHelper.vectorToBitmap(context, R.drawable.train, color)
        }
        return trainIcon
    }

    fun personIcon(context: Context): BitmapDescriptor {
        val personIcon: BitmapDescriptor by lazy {
            val color = ContextCompat.getColor(context, R.color.Cyan_700)
            BitmapHelper.vectorToBitmap(context, R.drawable.ic_profile, color)
        }
        return personIcon
    }
}

     val TAG = HomeFragment::class.java.simpleName
     const val DEFAULT_ZOOM = 15
     const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
     const val KEY_CAMERA_POSITION = "camera_position"
     const val KEY_LOCATION = "location"
        val defaultLocation = LatLng(24.582133783959872, 46.76407041289462)

