package com.bootcamp.stations.profile.domain

import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetUserProfileUseCase(private val profileRepositry: ProfileRepositry) {

    suspend operator fun invoke() = profileRepositry.getUserInfo()

}