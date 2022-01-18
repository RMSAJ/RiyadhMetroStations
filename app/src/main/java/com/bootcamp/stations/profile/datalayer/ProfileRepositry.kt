package com.bootcamp.stations.profile.datalayer

import android.net.Uri
import com.bootcamp.stations.DataState
import com.bootcamp.stations.UserModel
import com.bootcamp.stations.profile.model.ProfileModel

class ProfileRepositry(private val userProfileDataSource:ProfileInfoDataSource ) {

    suspend fun setUserInfo(profileModel: ProfileModel, uri: Uri?) : DataState {
       return userProfileDataSource.setUserInfo(profileModel, uri)
    }

    suspend fun getUserInfo() = userProfileDataSource.getUserInfo()

}