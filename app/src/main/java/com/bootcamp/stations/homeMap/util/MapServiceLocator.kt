package com.bootcamp.stations.homeMap.util

import android.util.Log
import com.bootcamp.stations.homeMap.dataLayer.remoteDataBase.MarkersDataSource
import com.bootcamp.stations.homeMap.dataLayer.remoteDataBase.MarkersRealTimeDataSource
import com.bootcamp.stations.homeMap.dataLayer.remoteDataBase.MarkersRepositry
import com.bootcamp.stations.homeMap.domain.GetMarkersUseCase
import com.google.firebase.database.FirebaseDatabase

object MapServiceLocator {

    private fun provideMarkerRealTimeDatasouce(): MarkersDataSource = MarkersRealTimeDataSource(
        FirebaseDatabase.getInstance()
    )
    private fun proviedMarkerRepositry(): MarkersRepositry {
        Log.e("TAG","mapserViceLocator at Repo" )

        return MarkersRepositry(provideMarkerRealTimeDatasouce())
    }

    fun providerGetMarkerUseCase(): GetMarkersUseCase =
        GetMarkersUseCase(proviedMarkerRepositry())
}