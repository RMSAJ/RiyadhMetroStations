package com.bootcamp.stations.homeMap.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.stations.favorite.ui.FavoriteUiState
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.google.android.gms.maps.model.LatLng

class BottomViewModel : ViewModel() {


    private val favListItem = mutableListOf<Place>()

   private val _favList: MutableLiveData<List<FavoriteUiState?>> = MutableLiveData(listOf(
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c","4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_3.jpg?alt=media&token=656ae9bd-3f2b-4112-a7a7-35673c1e372e","4E1","Norah Station", "Departure Time : 1:00 PM"),
FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c","4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_3.jpg?alt=media&token=656ae9bd-3f2b-4112-a7a7-35673c1e372e","4E1","Norah Station", "Departure Time : 1:00 PM"),
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c","4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_3.jpg?alt=media&token=656ae9bd-3f2b-4112-a7a7-35673c1e372e","4E1","Norah Station", "Departure Time : 1:00 PM"),
FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c","4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
       FavoriteUiState("https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_3.jpg?alt=media&token=656ae9bd-3f2b-4112-a7a7-35673c1e372e","4E1","Norah Station", "Departure Time : 1:00 PM")

   ))

     val fav: LiveData<List<FavoriteUiState?>> = _favList




//    fun newFav (name: String,latLng: LatLng,address: String) {
//
//        favAddByItem(returnItem(name,latLng,address))
//    }

    private fun favAddByItem(item : Place) {
        favListItem.add(item)
    }

//    private fun returnItem(
//        name: String,
//        latLng: LatLng,
//        address: String,
//        rating: Double = 5.0
//    ): Place {
//
//        return Place(name, latLng, address, rating)
//    }




}