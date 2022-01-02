package com.bootcamp.stations.user.util

import com.bootcamp.stations.user.dataLyer.UserDataSource
import com.bootcamp.stations.user.dataLyer.UserFireDataSource
import com.bootcamp.stations.user.dataLyer.UserRepositry
import com.bootcamp.stations.user.domain.AddUserUseCase
import com.google.firebase.firestore.FirebaseFirestore

object ServiceLocator {

     private fun provideUserFireStoreDataSource(): UserDataSource = UserFireDataSource(
        FirebaseFirestore.getInstance()
        )
    private fun provideUserRepositry(): UserRepositry =
        UserRepositry(provideUserFireStoreDataSource()
        )
    fun provideAddUserUseCase(): AddUserUseCase =
        AddUserUseCase(provideUserRepositry())
}