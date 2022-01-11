package com.bootcamp.stations.homeMap.ui

import com.google.android.gms.maps.model.LatLng

data class MapUiState(val marker:Map<LineUiStates,List<MarkerItemUIStatus>> = mapOf(),)


data class MarkerItemUIStatus(val id: Int? = 0,
                              val name: String?= "",
                              val latLng: LatLng = LatLng(123.002,21.00),
                              val address: String = ""

)

data class LineUiStates(val name:String="",val color: String="#ffffff",val width:Float=10f)

