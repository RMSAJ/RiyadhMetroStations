package com.bootcamp.stations.favorite.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.stations.favorite.util.FavoriteServiceLocator.provideGetFavorites
import com.bootcamp.stations.favorite.util.FavoriteServiceLocator.provideSetFavoriteUseCase
import com.bootcamp.stations.profile.model.ProfileViewModel
import com.bootcamp.stations.profile.util.ProfileServiceLocator
import java.lang.IllegalArgumentException

class FavoriteViewModelFactory : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(BottomSheetViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BottomSheetViewModel(
                    provideSetFavoriteUseCase()
                ) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(
                    provideGetFavorites()
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}