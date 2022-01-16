package com.bootcamp.stations.favorite.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.ui.FavoriteUiState
import com.bootcamp.stations.favorite.util.Constants.IMAGE1
import com.bootcamp.stations.favorite.util.Constants.IMAGE2
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(private val getFavoritesUseCase: GetFavoritesUseCase) : ViewModel() {

    init {
        getFavs()
    }

    private var _favoriteList: MutableStateFlow<List<FavoriteUiState>> = MutableStateFlow(listOf(FavoriteUiState()))
    val favoriteList = _favoriteList.asStateFlow()

    private val favListItem = mutableListOf<Place>()

   private val _favList: MutableLiveData<List<FavoriteUiState?>> = MutableLiveData(listOf(
//       FavoriteUiState(IMAGE1,"4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE2,"4E1","Norah Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE1,"4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE2,"4E1","Norah Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE1, "4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE2,"4E1","Norah Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE1,"4D1","Tuwaiq Station", "Departure Time : 1:00 PM"),
//       FavoriteUiState(IMAGE2,"4E1","Norah Station", "Departure Time : 1:00 PM")

   ))

     val fav: LiveData<List<FavoriteUiState?>> = _favList


//    fun newFav (name: String,latLng: LatLng,address: String) {
//
//        favAddByItem(returnItem(name,latLng,address))
//    }

    private fun favAddByItem(item : Place) {
        favListItem.add(item)
    }

    private fun getFavs() {
        viewModelScope.launch {
          val facorites =  getFavoritesUseCase.invoke()
            facorites.collect{
                val listOfFavs = mutableListOf<FavoriteUiState>()

                it.forEach { item ->
                    listOfFavs.add(FavoriteUiState( id = item.id!!, location =  item.location!!, title = item.markerTitle!!  ))
                }
                _favoriteList.update { listOfFavs }
            }
        }
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