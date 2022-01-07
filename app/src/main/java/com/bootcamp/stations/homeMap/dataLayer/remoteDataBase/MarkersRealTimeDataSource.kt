package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

import android.location.Location
import android.util.Log
import com.bootcamp.stations.homeMap.dataLayer.data.GeometryLocation
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.bootcamp.stations.homeMap.dataLayer.data.PlaceResponse
import com.bootcamp.stations.homeMap.dataLayer.data.toPlace
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.type.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class MarkersRealTimeDataSource(
    private val realTimeDataFireStore: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MarkersDataSource {
    private var place = PlaceResponse(
        0, GeometryLocation(24.732699, 46.696952), "", "1.1", 2.2
    )


    override suspend fun getMarkers(): Flow<List<Place>> = callbackFlow {
        realTimeDataFireStore.getReference("data")
            .child("markers").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("TAG", snapshot.toString())
                    var list = snapshot.children.map {
                        val point = it.getValue(PlaceResponse::class.java)!!
                      PlaceResponse(
                            id = point.id,
                            location = point.location,
                            name = point.name,
                            vicinity = point.vicinity,
                            rating = point.rating,
                            line = point.line
                        ).toPlace()
                    }
                    trySend(list)

                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
            )
        awaitClose { }


    }

}