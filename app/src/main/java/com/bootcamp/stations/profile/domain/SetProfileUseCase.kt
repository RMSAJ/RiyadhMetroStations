package com.bootcamp.stations.profile.domain

import android.net.Uri
import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import com.bootcamp.stations.profile.model.ProfileModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SetProfileUseCase(private val profileRepositry: ProfileRepositry) {

    suspend operator fun invoke(profileModel: ProfileModel, uri: Uri?) =

    profileRepositry.setUserInfo(profileModel,uri)
}


