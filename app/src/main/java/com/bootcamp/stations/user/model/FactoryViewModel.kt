package com.bootcamp.stations.user.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.stations.user.util.UserServiceLocator
import java.lang.IllegalArgumentException
class FactoryViewModel:ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

         when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(UserServiceLocator.provideAddUserUseCase()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}

