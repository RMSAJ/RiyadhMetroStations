package com.bootcamp.stations.homeMap.dataLayer.data

import android.content.Context
import com.bootcamp.stations.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader

class PlacesReader(private val context: Context) {

    private val gson = Gson()
    /**  change only the Json file [places]  */
    private val inputStream: InputStream
        get() = context.resources.openRawResource(R.raw.map_locations) // getting the json file

    // read the json file into the map
    fun read(): List<Place> {
        val itemType = object : TypeToken<List<PlaceResponse>>() {}.type
        val reader = InputStreamReader(inputStream)
        return gson.fromJson<List<PlaceResponse>>(reader, itemType).map {
            it.toPlace()
        }
    }
}