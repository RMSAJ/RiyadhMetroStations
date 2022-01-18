package com.bootcamp.stations.profile.datalayer

import android.net.Uri
import com.bootcamp.stations.DataState
import com.bootcamp.stations.UserModel
import com.bootcamp.stations.profile.model.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class ProfileRepositry(private val userProfileDataSource:ProfileInfoDataSource ) {

    suspend fun setUserInfo(profileModel: ProfileModel, uri: Uri?):Flow<DataState> =
      userProfileDataSource.setUserInfo(profileModel, uri)

        suspend fun getUserInfo() = userProfileDataSource.getUserInfo()


}