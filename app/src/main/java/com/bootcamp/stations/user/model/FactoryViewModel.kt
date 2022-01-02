package com.bootcamp.stations.user.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.stations.user.UserViewModel
import com.bootcamp.stations.user.domain.AddUserUseCase
import com.bootcamp.stations.user.util.ServiceLocator.provideAddUserUseCase
import java.lang.IllegalArgumentException

class FactoryViewModel:ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

         when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(provideAddUserUseCase()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}

