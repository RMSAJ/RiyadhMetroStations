package com.bootcamp.stations.favorite.util

import com.bootcamp.stations.favorite.datalyer.FavoriteDataSource
import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.datalyer.UserFavoriteFireDataSource
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.bootcamp.stations.profile.datalayer.ProfileFireStoreDataSource
import com.bootcamp.stations.profile.datalayer.ProfileInfoDataSource
import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import com.bootcamp.stations.profile.domain.GetUserProfileUseCase
import com.bootcamp.stations.profile.domain.SetProfileUseCase
import com.bootcamp.stations.profile.util.ProfileServiceLocator
import com.google.firebase.firestore.FirebaseFirestore

object FavoriteServiceLocator {

    private fun provideUserFavoriteFireStoreSource():
            FavoriteDataSource = UserFavoriteFireDataSource(FirebaseFirestore.getInstance())

    private fun provideFavoriteRepositry(): FavoriteRepositry =

        FavoriteRepositry(provideUserFavoriteFireStoreSource())

    fun provideSetFavoriteUseCase(): AddToFavoriteUseCase =
        AddToFavoriteUseCase(provideFavoriteRepositry())
    //endregion

    //region get user Info

//    fun provideGetProfileUseCase(): GetUserProfileUseCase =
//        GetUserProfileUseCase(provideProfileRepositry())
}