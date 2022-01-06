package com.bootcamp.stations.homeMap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.stations.homeMap.util.MapServiceLocator.providerGetMarkerUseCase
import java.lang.IllegalArgumentException

class MapViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
           if (modelClass.isAssignableFrom(MapViewModel::class.java)){
               return MapViewModel(
                   providerGetMarkerUseCase()
               )as T
           }
        else {throw IllegalArgumentException("Unkown data source")  }
    }

}