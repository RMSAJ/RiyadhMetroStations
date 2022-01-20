package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.bootcamp.stations.homeMap.dataLayer.data.PlaceResponse
import kotlinx.coroutines.flow.Flow

interface MarkersDataSource {
    suspend fun getMarkers(): Flow<List<Place>>
}