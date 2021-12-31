package com.bootcamp.stations.user.dataLyer

import com.bootcamp.stations.user.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserFireDataSource(
    private val fireStoreDB: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
):UserDataSource {
    override suspend fun addUSer(userModel: UserModel) {
        fireStoreDB.collection("User")
            .document(userModel.email!!).set(userModel)
            .addOnCompleteListener {
                println("success Registration =  $it")
            }
            .addOnFailureListener {
                println("Failure Registration =  $it")
            }
    }

    override suspend fun getUSer(userModel: UserModel) {
        fireStoreDB.document(userModel.email!!)
            .get()
            .addOnCompleteListener {
                println("success Registration =  $it")
            }
            .addOnFailureListener {
                println("Failure Registration =  $it")
            }
    }


}