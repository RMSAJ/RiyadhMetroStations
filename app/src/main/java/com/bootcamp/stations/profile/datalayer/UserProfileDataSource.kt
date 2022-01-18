package com.bootcamp.stations.profile.datalayer

import android.net.Uri
import android.util.Log
import com.bootcamp.stations.Constants.PROFILE
import com.bootcamp.stations.DataState
import com.bootcamp.stations.LOADING_STATUS
import com.bootcamp.stations.UserModel
import com.bootcamp.stations.profile.model.ProfileModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class ProfileFireStoreDataSource(private val fireStoreDB: FirebaseFirestore):ProfileInfoDataSource {

    val auth = Firebase.auth.currentUser?.email
    private val db = fireStoreDB

    override suspend fun setUserInfo(profileModel: ProfileModel, uri: Uri?): Flow<DataState> = callbackFlow {
        if (uri ==  null){
        val user = db.collection("User").document("$auth")
            user.set(mapOf(PROFILE to profileModel), SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful)
                trySend(DataState(LOADING_STATUS.DONE))
            }
            .addOnFailureListener {
                trySend(DataState(LOADING_STATUS.ERROR, "${it.message}"))
            }

    }else{
        if (uri.toString().contains("https:")){
            val user = db.collection("User").document("$auth")
            user.set(mapOf(PROFILE to profileModel), SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful)
                    trySend(DataState(LOADING_STATUS.DONE))
                }
                .addOnFailureListener {
                    trySend(DataState(LOADING_STATUS.ERROR, "${it.message}"))
                }

        }else{
            upload(uri).collect{
                profileModel.profileImage = it.toString()

                val user = db.collection("User").document("$auth")
                user.set(mapOf(PROFILE to profileModel), SetOptions.merge())
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        trySend(DataState(LOADING_STATUS.DONE))
                    }
                    .addOnFailureListener {
                        trySend(DataState(LOADING_STATUS.ERROR, "${it.message}"))
                    }

            }
        }
        }

        awaitClose {  }
    }

    override suspend fun getUserInfo():Flow<ProfileModel> = callbackFlow {
       db.collection("User").document("$auth").get().addOnCompleteListener{
           val userInfo = it.result.toObject(UserModel::class.java)
           if (userInfo != null) {
               userInfo.profile?.let {  trySend(it) }
               Log.d("TAG", "getUserInfo: success = ${it}")
           }
           Log.d("TAG", "why null man: ${it.result} ")
       }.addOnFailureListener {
            Log.d("TAG", "getUserInfo: failure: ${it.message} ")
        }
        awaitClose{}
    }

    private suspend fun upload(file: Uri): Flow<Uri> = callbackFlow {

        val firestore = Calendar.getInstance().timeInMillis
        val storageRef = FirebaseStorage.getInstance().getReference("/images/$firestore")

        storageRef.putFile(file).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { imageUri ->
                Log.e("TAG", "imageUrl:${imageUri}")
                trySend(imageUri)

            }

        }

        awaitClose { }

    }


}