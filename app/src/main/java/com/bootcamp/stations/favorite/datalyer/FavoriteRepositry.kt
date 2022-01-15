package com.bootcamp.stations.favorite.datalyer

import com.bootcamp.stations.favorite.model.FavoriteModel
import com.bootcamp.stations.user.model.UserModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class FavoriteRepositry (private val favoriteDataSource: FavoriteDataSource) {

    suspend fun setfavorite(listofFavoriteModel: List<FavoriteModel>) {

        favoriteDataSource.addFave(listofFavoriteModel)

    }

    suspend fun getFavorite(): Flow<List<FavoriteModel>> =
        favoriteDataSource.getFav()



}