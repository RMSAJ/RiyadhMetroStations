package com.bootcamp.stations.profile.datalayer

import android.net.Uri
import com.bootcamp.stations.DataState
import com.bootcamp.stations.UserModel
import com.bootcamp.stations.profile.model.ProfileModel
import kotlinx.coroutines.flow.Flow


interface ProfileInfoDataSource {

suspend fun setUserInfo(profileModel: ProfileModel, uri: Uri?): Flow<DataState>

suspend fun getUserInfo(): Flow<ProfileModel>

}