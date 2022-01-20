package com.bootcamp.stations.user.domain

import com.bootcamp.stations.user.dataLyer.UserRepositry
import com.bootcamp.stations.user.model.UserModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

class AddUserUseCase(private val userRepositry: UserRepositry,
                     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(userModel: UserModel) =
        userRepositry.addUser(userModel)

}