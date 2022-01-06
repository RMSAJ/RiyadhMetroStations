package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

import com.bootcamp.stations.homeMap.dataLayer.data.PlaceResponse

interface MarkersDataSource {
    suspend fun getMarkers(): PlaceResponse
}