package com.bootcamp.stations.favorite.model

import com.google.android.gms.maps.model.LatLng

data class FavoriteModel (val id: String? = null,
                          val markerTitle: String? = null,
                          val location: LatLng? = null
                          )