package com.bootcamp.stations.favorite.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val addToFavoriteUseCase: AddToFavoriteUseCase) :ViewModel() {

    fun addToFavorite(markerId: String, title: String, location: LatLng){
        viewModelScope.launch {
            addToFavoriteUseCase.invoke(markerId,title,location)
        }
    }

}