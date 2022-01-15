package com.bootcamp.stations.user.model

import com.bootcamp.stations.favorite.model.FavoriteModel

data class UserModel(
    var email: String? = "",
    var favorite: MutableList<FavoriteModel> = mutableListOf()
    )

data class GeometryLocation(
    var lat: Double,
    var lng: Double
)
