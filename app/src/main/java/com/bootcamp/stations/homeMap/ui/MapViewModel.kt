package com.bootcamp.stations.homeMap.ui


import android.location.Location
import android.util.Log

import androidx.lifecycle.*
import com.bootcamp.stations.homeMap.dataLayer.data.*

import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.PolylineOptions
import com.bootcamp.stations.homeMap.domain.GetMarkersUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    private val getMarkersUseCase: GetMarkersUseCase
) : ViewModel() {

    private val _mapMarkersByLine = mutableMapOf<Line, MutableList<Place>>()
    val mapMarkersByLine get() = _mapMarkersByLine

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
    }

//    fun fakemakers() {
//
//
//        val mapitem: MutableMap<LineUiStates, List<MarkerItemUIStatus>> = mutableMapOf()
//        val list = listOf(
//            MarkerItemUIStatus(latLng = LatLng(24.5504623769522, 46.9874), name = "Zamel"),
//            MarkerItemUIStatus(latLng = LatLng(25.5504623769522, 46.9874), name = "Zaeeeeemel"),
//            MarkerItemUIStatus(
//                latLng = LatLng(26.5504623769522, 46.9874), name = "Zaerrmel"
//            )
//        )
//        mapitem[LineUiStates("Zamel Line", "#0000ff", 18f)] = list
//        _mapUiState.update { it.copy(marker = mapitem) }
//    }

    fun getMarkers() {
        viewModelScope.launch {
            Log.e("TAG", "getMarkers ViewModel")
            val placeMarker = getMarkersUseCase.invoke()

            placeMarker.collect {


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