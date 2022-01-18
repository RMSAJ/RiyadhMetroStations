package com.bootcamp.stations.profile.domain

import android.net.Uri
import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import com.bootcamp.stations.profile.model.ProfileModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetProfileUseCase(private val profileRepositry: ProfileRepositry) {

    suspend operator fun invoke(profileModel: ProfileModel, uri: Uri?):Boolean {
        var onSendingData = false
        withContext(Dispatchers.IO) {
            profileRepositry.getUserInfo().collect {
                val profile = it
                profile.let {
                    profile.copy(
                        profilePhone = profileModel.profilePhone,
                        profileImage = profileModel.profileImage,
                        profileName = profileModel.profileName,
                        profileEmail = profileModel.profileEmail
                    )
                }
                onSendingData   =    profileRepositry.setUserInfo(profile, uri)

            }

//            profileRepositry.setUserInfo(profileModel, uri)
        }
        return onSendingData
    }
}


