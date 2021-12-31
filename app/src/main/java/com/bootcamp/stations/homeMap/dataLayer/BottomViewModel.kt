package com.bootcamp.stations.homeMap.dataLayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.google.android.gms.maps.model.LatLng

class BottomViewModel : ViewModel() {


    val favListItem = mutableListOf<Place>()
    private val _favList = MutableLiveData<List<Place?>?>()
    val favList: LiveData<List<Place?>?> = _favList





    fun newFav (name: String,latLng: LatLng,address: String) {

        favAddByItem(returnItem(name,latLng,address))
    }

    private fun favAddByItem(item : Place) {
        favListItem.add(item)
    }

    private fun returnItem(name: String,latLng: LatLng,address: String, rating :Double = 5.0) : Place {

        val item = Place(name,latLng,address,rating)
        return item
    }




}