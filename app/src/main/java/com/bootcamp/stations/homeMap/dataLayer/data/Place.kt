package com.bootcamp.stations.homeMap.dataLayer.data

import com.google.android.gms.maps.model.LatLng

data class Place( val id: Int? = 0,
                val name: String?= "",
                 val latLng: LatLng =LatLng(123.002,21.00) ,
                 val address: String = "",
                 val rating: Double = 0.0,
                val line:Line=Line())

