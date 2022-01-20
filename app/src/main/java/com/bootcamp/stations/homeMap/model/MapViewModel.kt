package com.bootcamp.stations.homeMap.model


import android.location.Location
import android.util.Log

import androidx.lifecycle.*
import com.bootcamp.stations.homeMap.dataLayer.data.*

import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.PolylineOptions
import com.bootcamp.stations.homeMap.domain.GetMarkersUseCase
import com.bootcamp.stations.homeMap.ui.LineUiStates
import com.bootcamp.stations.homeMap.ui.MapUiState
import com.bootcamp.stations.homeMap.ui.MarkerItemUIStatus
import com.google.android.gms.maps.model.RoundCap
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    private val getMarkersUseCase: GetMarkersUseCase
) : ViewModel() {

//    private val _mapMarkersByLine = mutableMapOf<Line, MutableList<Place>>()
//    val mapMarkersByLine get() = _mapMarkersByLine

    //    private lateinit var auth: FirebaseAuth
    private var _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState = _mapUiState.asStateFlow()

    private val _locationPermissionGranted = MutableLiveData<Boolean>()
    val locationPermissionGranted: LiveData<Boolean> get() = _locationPermissionGranted

    private val _lastKnownLocation =  MutableLiveData<Location>()

    val lastKnownLocation get() = _lastKnownLocation

    fun isPermitionGranted(locationPermissionGranted: Boolean) {
        _locationPermissionGranted.value = locationPermissionGranted
    }


    init {
        getMarkers()
    }

    fun setLastKnownLocation(location: Location?) {
        _lastKnownLocation.value = location!!
    }

    fun addNewPolyline(
        listOfPoint: List<LatLng>,
        width: Float,
        color: Int
    ): PolylineOptions {
        return PolylineOptions()
            .clickable(true)
            .addAll(listOfPoint)
            .width(width)
            .color(color)
//            .jointType(RoundCap().describeContents())
    }



    private fun getMarkers() {
        viewModelScope.launch {
            Log.e("TAG", "getMarkers ViewModel")
            val placeMarker = getMarkersUseCase.invoke()

            placeMarker.collect { it ->


                val mapMarkersByLine = mutableMapOf<LineUiStates, MutableList<MarkerItemUIStatus>>()
                it.forEach { place ->
                    val line = LineUiStates(place.line.name, place.line.color, place.line.width)
                    val markerItemUIStatus =
                        MarkerItemUIStatus(place.id, place.name, place.latLng, place.address)

                    if (mapMarkersByLine[line] == null) {
                        mapMarkersByLine[line] = mutableListOf()
                    }
                    mapMarkersByLine[line]?.add(markerItemUIStatus)
                }
                _mapUiState.update { it.copy(marker = mapMarkersByLine) }
            }
        }

    }
}