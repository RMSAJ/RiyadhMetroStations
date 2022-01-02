package com.bootcamp.stations.user.dataLyer

import com.bootcamp.stations.user.model.UserModel

class UserRepositry(private val userDataSource: UserDataSource) {
    suspend fun addUser(userModel: UserModel) =
        userDataSource.addUSer(userModel)
}