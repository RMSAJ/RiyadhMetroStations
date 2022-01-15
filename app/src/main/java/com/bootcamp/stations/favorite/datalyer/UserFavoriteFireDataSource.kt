package com.bootcamp.stations.favorite.datalyer

import android.util.Log
import com.bootcamp.stations.favorite.model.FavoriteModel
import com.bootcamp.stations.favorite.model.FavoriteRemote
import com.bootcamp.stations.favorite.util.Constants.FAVOURITE
import com.bootcamp.stations.user.dataLyer.UserDataSource
import com.bootcamp.stations.user.model.UserModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.log

class UserFavoriteFireDataSource
    (private val fireStoreDB: FirebaseFirestore,
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): FavoriteDataSource {

    private val auth = Firebase.auth.currentUser
    private val db = fireStoreDB

    override suspend fun addFave(listofFavoriteModel: List<FavoriteModel>) {
     val fav =   db.collection("User").document("${auth?.email}")
        fav.set( mapOf(FAVOURITE to listofFavoriteModel.toMutableList().toMutableSet().toList()))
//        fav.update( FAVOURITE, listofFavoriteModel)
        Log.e("TAG", "addFave: to FireStore $listofFavoriteModel ")
    }

    override suspend fun getFav(): Flow<List<FavoriteModel>> = callbackFlow {

        auth.apply {
//            val favortieList = mutableListOf<MutableList<FavoriteModel>>()

            val getFav = db.collection("User").document("${auth?.email}")

            getFav.get().addOnCompleteListener {
                Log.e("TAG", "Getting favoritewwwws: ${it.result.data} ")


                val favorite = it.result.toObject(FavoriteRemote::class.java)

//

//                FavoriteModel(
//                    id = favorite?.id,
//                    markerTitle = favorite?.markerTitle,
//                    location = favorite?.location
//                )

            Log.d("TAG", "Getting favoritesiii: ${favorite?.Favorite}")
            trySend(favorite?.Favorite ?: emptyList())
         }

        }
        awaitClose { }
    }

}