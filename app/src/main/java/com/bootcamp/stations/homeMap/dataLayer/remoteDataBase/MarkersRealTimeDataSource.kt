package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

import android.util.Log
import com.bootcamp.stations.homeMap.dataLayer.data.PlaceResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarkersRealTimeDataSource(
    private val realTimeDataFireStore: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :MarkersDataSource {
 private  var  place = PlaceResponse()
    override suspend fun getMarkers(): PlaceResponse = withContext(ioDispatcher) {
         realTimeDataFireStore.getReference("data").
         child("markers").addValueEventListener(object: ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 Log.e("TAG",snapshot.toString() )
                 for (marker in snapshot.children) {
                     val marker = snapshot.getValue(PlaceResponse::class.java)
                        place = PlaceResponse(id = marker?.id ,
                            location = marker!!.location,
                            name = marker.name,
                            vicinity = marker.vicinity,
                            rating = marker.rating,
                            line = marker.line
                        )
                     Log.e("TAG",marker.toString() )

                 }
             }
             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }
         }
         )
        place


    }

}