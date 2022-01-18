package com.bootcamp.stations.profile.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.stations.profile.util.ProfileServiceLocator.provideGetProfileUseCase
import com.bootcamp.stations.profile.util.ProfileServiceLocator.provideSetProfileUseCase
import java.lang.IllegalArgumentException

class ProfileViewModelFactory: ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(provideSetProfileUseCase(),
                    provideGetProfileUseCase()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}