package com.bootcamp.stations.homeMap.dataLayer.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place( val id: Int?= 0,
                val name: String? ="",
                 val latLng: LatLng?= null,
                 val address: String? = "",
                 val rating: Double? = 1.1,
                val line:Line=Line())

