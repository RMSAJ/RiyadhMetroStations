package com.bootcamp.stations.favorite.model


data class FavoriteModel (val id: String? = null,
                          val location: Map<String,Double>? = null,
                          val markerTitle: String? = null,
                          var isFavorite: Boolean = false  )

data class FavoriteRemote ( val Favorite: List<FavoriteModel> = listOf()
)

