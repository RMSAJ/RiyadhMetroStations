package com.bootcamp.stations.user.dataLyer

import com.bootcamp.stations.user.model.UserModel

interface UserDataSource {

    suspend fun addUSer(userModel: UserModel)

    suspend fun getUSer(userModel: UserModel)

}