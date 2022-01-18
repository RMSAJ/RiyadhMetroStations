package com.bootcamp.stations.homeMap.domain

import com.bootcamp.stations.homeMap.dataLayer.remoteDataBase.MarkersRepositry

class GetMarkersUseCase(private val markersRepositry: MarkersRepositry) {

    suspend operator fun invoke() = markersRepositry.getMarkers()
}