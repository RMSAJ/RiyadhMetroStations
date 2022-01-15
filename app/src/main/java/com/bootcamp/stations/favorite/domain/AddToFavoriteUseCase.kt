package com.bootcamp.stations.favorite.domain

import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.model.FavoriteModel
import com.bootcamp.stations.user.model.UserModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddToFavoriteUseCase(private val favoriteRepositry: FavoriteRepositry,
                           private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

suspend operator fun invoke(id: String, title: String, location: LatLng) = withContext(ioDispatcher){

    favoriteRepositry.getFavorite().collect{
        val favoriteList = it.toMutableList()

        favoriteList.add(FavoriteModel(id = id,title,location))
        favoriteRepositry.setfavorite(favoriteList)
    }
}


//favoriteRepositry.setfavorite(id,title,location)

}