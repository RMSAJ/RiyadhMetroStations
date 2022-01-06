package com.bootcamp.stations.homeMap.dataLayer.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place( val id: Int?,
                val name: String?,
                 val latLng: LatLng,
                 val address: String,
                 val rating: Double,
                val line:Line=Line())

