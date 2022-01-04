package com.bootcamp.stations.homeMap.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.bootcamp.stations.R
import com.google.android.gms.maps.model.BitmapDescriptor

fun trainIcon (context: Context):BitmapDescriptor {
     val trainIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.Primary_Green_900)
        BitmapHelper.vectorToBitmap(context, R.drawable.train, color)
    }
    return trainIcon
}

fun personIcon(context: Context):BitmapDescriptor {
     val personIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.Cyan_700)
        BitmapHelper.vectorToBitmap(context, R.drawable.ic_profile, color)
    }
    return personIcon
}


