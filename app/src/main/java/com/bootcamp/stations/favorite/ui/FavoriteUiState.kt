package com.bootcamp.stations.favorite.ui

import com.google.android.gms.maps.model.LatLng

data class FavoriteUiState(val image:String = "https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c",
                           val id: String= "",
                           val location: Map<String,Double>?= null ,
                           val title: String=""
)

data class IsFavoriteUiState(val isfavorite: Boolean = false)
