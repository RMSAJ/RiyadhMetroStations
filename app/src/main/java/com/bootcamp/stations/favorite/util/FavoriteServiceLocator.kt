package com.bootcamp.stations.favorite.util

import com.bootcamp.stations.favorite.datalyer.FavoriteDataSource
import com.bootcamp.stations.favorite.datalyer.FavoriteRepositry
import com.bootcamp.stations.favorite.datalyer.UserFavoriteFireDataSource
import com.bootcamp.stations.favorite.domain.AddToFavoriteUseCase
import com.bootcamp.stations.favorite.domain.GetFavoritesUseCase
import com.bootcamp.stations.favorite.domain.RemoveFavUseCase
import com.bootcamp.stations.profile.datalayer.ProfileFireStoreDataSource
import com.bootcamp.stations.profile.datalayer.ProfileInfoDataSource
import com.bootcamp.stations.profile.datalayer.ProfileRepositry
import com.bootcamp.stations.profile.domain.GetUserProfileUseCase
import com.bootcamp.stations.profile.domain.SetProfileUseCase
import com.bootcamp.stations.profile.util.ProfileServiceLocator
import com.google.firebase.firestore.FirebaseFirestore

object FavoriteServiceLocator {


    //region add to Favorite
    private fun provideUserFavoriteFireStoreSource():
            FavoriteDataSource = UserFavoriteFireDataSource(FirebaseFirestore.getInstance())

    private fun provideFavoriteRepositry(): FavoriteRepositry =

        FavoriteRepositry(provideUserFavoriteFireStoreSource())

    fun provideSetFavoriteUseCase(): AddToFavoriteUseCase =
        AddToFavoriteUseCase(provideFavoriteRepositry())

    fun provideRemoveFavUseCase():RemoveFavUseCase =
        RemoveFavUseCase(provideFavoriteRepositry())
    //endregion

    //region get favorites Info

    fun provideGetFavorites():GetFavoritesUseCase =
        GetFavoritesUseCase(provideFavoriteRepositry())

//    fun provideGetProfileUseCase(): GetUserProfileUseCase =
//        GetUserProfileUseCase(provideProfileRepositry())
}