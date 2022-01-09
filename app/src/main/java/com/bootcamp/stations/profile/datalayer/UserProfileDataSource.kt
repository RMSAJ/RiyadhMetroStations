package com.bootcamp.stations.profile.datalayer

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ProfileFireStoreDataSource(private val fireStoreDB: FirebaseFirestore,
                            private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO ) {



}