package com.bootcamp.stations.profile.model

import com.bootcamp.stations.favorite.model.FavoriteModel

data class ProfileModel(
                        var profileImage: String = "",
                        val profileName:String ="",
                        val profileEmail: String= "",
                        val profilePhone: String= "",
)
