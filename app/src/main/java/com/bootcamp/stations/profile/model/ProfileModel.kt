package com.bootcamp.stations.profile.model

import com.bootcamp.stations.favorite.model.FavoriteModel

data class ProfileModel(
    val profileEmail: String = "",
    val profileName:String ="",
    var profileImage: String = "",
    val profilePhone: String= "",
)
