package com.bootcamp.stations.favorite.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.DataState
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.ui.FavoriteUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val addToFavoriteUseCase: AddToFavoriteUseCase) :ViewModel() {

    private var _uiStatus = MutableStateFlow(DataState())
    val uiStatus: LiveData<DataState> = _uiStatus.asLiveData()



    private var _favoriteList = MutableStateFlow(mutableListOf<FavoriteUiState>())
    val favoriteList = _favoriteList.asStateFlow()

    fun addToFavorite(markerId: String, title: String, location: LatLng){
        viewModelScope.launch {
            addToFavoriteUseCase.invoke(markerId,title,location)
        }
    }
//    private fun getFavList() {
//
//        viewModelScope.launch {
//
//            val facorites =  getFavoritesUseCase.invoke()
//
//            facorites.collect{
//
//                val listFavs = mutableListOf<FavoriteUiState>()
//
//                it.forEach {
//                    val uiState =
//                        FavoriteUiState(id = it.id!!, location = it.location, title = it.markerTitle!!)
//                    listFavs.add(uiState)
//
//            }
//            _favoriteList.value = listFavs
//        }
//
//        }
//
//    }



//    init {
//        getFavList()
//    }


}