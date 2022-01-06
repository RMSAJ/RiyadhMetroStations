package com.bootcamp.stations.homeMap.dataLayer.remoteDataBase

import android.location.Location
import android.util.Log
import com.bootcamp.stations.homeMap.dataLayer.data.GeometryLocation
import com.bootcamp.stations.homeMap.dataLayer.data.PlaceResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.type.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarkersRealTimeDataSource(
    private val realTimeDataFireStore: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :MarkersDataSource {
 private  var  place = PlaceResponse(0, GeometryLocation( 24.732699, 46.696952)
     ,"","1.1",2.2)


    override suspend fun getMarkers(): PlaceResponse = withContext(ioDispatcher) {
         realTimeDataFireStore.getReference("data").
         child("markers").addValueEventListener(object: ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 Log.e("TAG",snapshot.toString() )
                 for (marker in snapshot.children) {
                 val point =  snapshot.getValue(PlaceResponse::class.java)!!
                        place = PlaceResponse(id = point.id,
                            location = point.location,
                            name = point.name,
                            vicinity = point.vicinity,
                            rating = point.rating,
                            line = point.line
                        )
                     Log.e("TAG",marker.toString() )

                 }
             }
             override fun onCancelled(error: DatabaseError) {
             }
         }
         )
        place


    }

}