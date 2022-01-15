package com.bootcamp.stations.favorite.domain

import android.util.Log
import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.model.FavoriteModel
import com.bootcamp.stations.user.model.UserModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddToFavoriteUseCase(private val favoriteRepositry: FavoriteRepositry) {

suspend operator fun invoke(markerId: String, title: String, location: LatLng) = withContext(Dispatchers.IO){

    Log.e("TAG", "addFave: to UseCase $title ")

        favoriteRepositry.getFavorite().collect {

        val favoriteList = it.toMutableList()
        Log.e("TAG", "addFave: to UseCase newList $favoriteList ")
        Log.e("TAG", "addFave: to UseCase newListeee $title ")

        favoriteList.add(FavoriteModel(markerId, mapOf("latitude" to location.latitude,"longitude" to location.longitude ),title))

        Log.e("TAG", "addFave: to UseCase final list to Repositry $favoriteList ")

        favoriteRepositry.setfavorite(favoriteList)
    }
  }
//favoriteRepositry.setfavorite(id,title,location)
}