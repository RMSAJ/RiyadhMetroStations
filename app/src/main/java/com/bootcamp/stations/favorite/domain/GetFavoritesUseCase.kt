package com.bootcamp.stations.favorite.domain

import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.model.FavoriteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFavoritesUseCase(private val favoriteRepositry: FavoriteRepositry)  {

    suspend operator fun invoke() = favoriteRepositry.getFavorite()
}