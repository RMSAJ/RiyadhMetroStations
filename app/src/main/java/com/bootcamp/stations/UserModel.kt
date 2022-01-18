package com.bootcamp.stations

import com.bootcamp.stations.favorite.model.FavoriteModel
import com.bootcamp.stations.profile.model.ProfileModel

data class UserModel(val Favorite: List<FavoriteModel> = listOf(),
                     val profile : ProfileModel? = null

)
