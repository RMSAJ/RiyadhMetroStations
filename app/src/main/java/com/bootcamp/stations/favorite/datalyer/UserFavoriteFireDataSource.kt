package com.bootcamp.stations.favorite.datalyer

import com.bootcamp.stations.favorite.model.FavoriteModel
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

class UserFavoriteFireDataSource
    (private val fireStoreDB: FirebaseFirestore,
     private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): FavoriteDataSource {

    private val auth = Firebase.auth.currentUser?.email
    private val db = fireStoreDB

    override suspend fun addFave(listofFavoriteModel: List<FavoriteModel>) {
     val fav =   db.collection("User").document("$auth")
//        fav.set( mapOf("Favorite" to listOf(id,title,location)), SetOptions.merge())
        .update( FAVOURITE, listofFavoriteModel)
    }

    override suspend fun getFav(): Flow<List<FavoriteModel>> = callbackFlow {
        val getFav = db.collection("User").document("$auth")
        getFav.get().addOnCompleteListener {
            val favorite: List<FavoriteModel>? = listOf(it.result.toObject(FavoriteModel::class.java)!!)

            trySend(favorite?:emptyList() )

        }
        awaitClose { }
    }


}