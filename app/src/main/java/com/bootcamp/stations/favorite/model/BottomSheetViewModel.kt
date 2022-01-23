package com.bootcamp.stations.favorite.model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.domain.RemoveFavUseCase
import com.bootcamp.stations.favorite.ui.FavoriteUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavUseCase: RemoveFavUseCase
) : ViewModel() {

    private var _favoriteList = MutableStateFlow(mutableListOf<FavoriteUiState>())
    val favoriteList = _favoriteList.asLiveData()

    init {
        getFavList()
    }

    fun addToFavorite(markerId: String, title: String, location: LatLng) {
        viewModelScope.launch {
            addToFavoriteUseCase.invoke(markerId, title, location)
        }
    }

    fun removeFav(markerId: String, title: String, location: LatLng) {
        viewModelScope.launch {
            removeFavUseCase.invoke(markerId, title, location)
        }
    }

    fun getFavList() {

        viewModelScope.launch {

            val facorites = getFavoritesUseCase.invoke()

            facorites.collect {

                val listFavs = mutableListOf<FavoriteUiState>()

                it.forEach {
                    val uiState =
                        FavoriteUiState(
                            id = it.id!!,
                            location = it.location,
                            title = it.markerTitle!!
                        )
                    listFavs.add(uiState)
                }
                _favoriteList.value = listFavs

            }
        }
    }
}