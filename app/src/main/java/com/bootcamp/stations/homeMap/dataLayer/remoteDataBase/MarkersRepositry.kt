package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

class MarkersRepositry (private val markersRealTimeDataSource: MarkersDataSource) {

    suspend fun getMarkers() = markersRealTimeDataSource.getMarkers()
}