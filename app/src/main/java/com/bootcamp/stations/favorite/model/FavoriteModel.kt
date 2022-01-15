package com.bootcamp.stations.favorite.model

import com.google.android.gms.maps.model.LatLng

data class FavoriteModel (val id: String? = null,
                          val location: Map<String,Double>? = null,
                          val markerTitle: String? = null,
                          )

data class FavoriteRemote(val Favorite: List<FavoriteModel> = listOf()
)