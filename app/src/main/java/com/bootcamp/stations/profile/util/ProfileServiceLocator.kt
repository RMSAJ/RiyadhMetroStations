package com.bootcamp.stations.profile.util

import com.bootcamp.stations.profile.datalayer.ProfileFireStoreDataSource
import com.bootcamp.stations.profile.datalayer.ProfileInfoDataSource
import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import com.bootcamp.stations.profile.domain.GetUserProfileUseCase
import com.bootcamp.stations.profile.domain.SetProfileUseCase
import com.google.firebase.firestore.FirebaseFirestore

object ProfileServiceLocator {

    //region Set The profile
    private fun provideUserProfileFireStoreSource():
ProfileInfoDataSource = ProfileFireStoreDataSource(FirebaseFirestore.getInstance())

  private fun provideProfileRepositry(): ProfileRepositry=

        ProfileRepositry(provideUserProfileFireStoreSource())

    fun provideSetProfileUseCase(): SetProfileUseCase =
        SetProfileUseCase(provideProfileRepositry())
    //endregion

    //region get user Info

    fun provideGetProfileUseCase(): GetUserProfileUseCase =
        GetUserProfileUseCase(provideProfileRepositry())

    //endregion

}