package com.bootcamp.stations.favorite.model

import androidx.lifecycle.*
import com.bootcamp.stations.DataState
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.ui.FavoriteUiState
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(private val getFavoritesUseCase: GetFavoritesUseCase) : ViewModel() {

    init {
        getFavs()
    }

    private var _favoriteList = MutableStateFlow(mutableListOf<FavoriteUiState>())
    val favoriteList = _favoriteList.asStateFlow()

    private var _uiStatus = MutableStateFlow(DataState())

    val uiStatus: LiveData<DataState> = _uiStatus.asLiveData()

//    private val favListItem = mutableListOf<Place>()
//
//    private fun favAddByItem(item : Place) {
//        favListItem.add(item)
//    }

     fun getFavs() {
        viewModelScope.launch {
          val facorites =  getFavoritesUseCase.invoke()
            facorites.collect{

                val listFavs = mutableListOf<FavoriteUiState>()
                it.forEach {
                    val uiState =
                        FavoriteUiState(id = it.id!!, location = it.location, title = it.markerTitle!!)
                    listFavs.add(uiState)
                }
                _favoriteList.value = listFavs
            }
        }
    }
}