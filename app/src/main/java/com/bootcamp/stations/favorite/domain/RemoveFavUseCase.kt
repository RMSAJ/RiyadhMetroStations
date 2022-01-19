package com.bootcamp.stations.favorite.domain

import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.model.FavoriteModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveFavUseCase(private val favoriteRepositry: FavoriteRepositry) {

    suspend operator fun invoke(markerId: String, title: String, location: LatLng)
            = withContext(Dispatchers.IO){

        favoriteRepositry.getFavorite().collect {

            val removeFav = it.find {
                it.markerTitle!!.contains(title) }
            val favoriteList = it.toMutableList()

            favoriteList.remove(removeFav)


            favoriteRepositry.setfavorite(favoriteList)


        }
    }
}