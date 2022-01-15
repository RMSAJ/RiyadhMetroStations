package com.bootcamp.stations.favorite.datalyer

import com.bootcamp.stations.favorite.model.FavoriteModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface FavoriteDataSource {
    suspend fun addFave(listofFavoriteModel: List<FavoriteModel>)

    suspend fun getFav(): Flow<List<FavoriteModel>>

}