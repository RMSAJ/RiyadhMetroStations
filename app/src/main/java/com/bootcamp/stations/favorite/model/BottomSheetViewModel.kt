package com.bootcamp.stations.favorite.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.DataState
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.ui.IsFavoriteUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val addToFavoriteUseCase: AddToFavoriteUseCase) :ViewModel() {

    private var _uiStatus = MutableStateFlow(DataState())
    val uiStatus: LiveData<DataState> = _uiStatus.asLiveData()

    private var _favoriteUiState = MutableStateFlow(IsFavoriteUiState())
    val favoriteUiState = _favoriteUiState



    fun addToFavorite(markerId: String, title: String, location: LatLng){
        viewModelScope.launch {
            addToFavoriteUseCase.invoke(markerId,title,location)
        }
    }
//    fun isFavorite(){
//        viewModelScope.launch {
//            isFavorite.invoke()
//        }
//
//    }


}