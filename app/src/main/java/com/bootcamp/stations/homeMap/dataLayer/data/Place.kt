package com.bootcamp.stations.homeMap.dataLayer.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place(val name: String,
                 val latLng: LatLng,
                 val address: String,
                 val rating: Double = 1.1)

