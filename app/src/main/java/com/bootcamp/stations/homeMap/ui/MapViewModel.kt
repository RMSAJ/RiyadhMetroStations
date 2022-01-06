package com.bootcamp.stations.homeMap.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.bootcamp.stations.homeMap.dataLayer.data.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.bootcamp.stations.homeMap.domain.GetMarkersUseCase
import com.bootcamp.stations.homeMap.util.Constants
import com.bootcamp.stations.homeMap.util.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.bootcamp.stations.homeMap.util.getDeviceLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val getMarkersUseCase: GetMarkersUseCase
) :ViewModel() {

    private val _mapMarkersByLine= mutableMapOf<Line,MutableList<Place>>()
    val mapMarkersByLine get() = _mapMarkersByLine

    //    private lateinit var auth: FirebaseAuth
private var _listOfMarkers = MutableStateFlow(Markers())
    val listOfMarkers get() = _listOfMarkers.asLiveData()

    init {
        getMarkers()
    }
    private val _locationPermissionGranted= MutableLiveData<Boolean>()
    val locationPermissionGranted: LiveData<Boolean> get() = _locationPermissionGranted

    private val _lastKnownLocation : MutableLiveData<Location?>? = null

    val lastKnownLocation get() = _lastKnownLocation

    fun isPermitionGranted(locationPermissionGranted:Boolean ) {
        _locationPermissionGranted.value = locationPermissionGranted

    }

     fun setLastKnownLocation(location: Location?) {
         _lastKnownLocation?.value = location
     }

     fun getTheDeviceLocation (context: FragmentActivity, googleMap: GoogleMap,
                                      fusedLocationProviderClient: FusedLocationProviderClient) {
        getDeviceLocation(context, locationPermissionGranted.value!!,googleMap,
            lastKnownLocation?.value!!,fusedLocationProviderClient)

    }

    private fun addPolyLine(googleMap: GoogleMap, places: Map<Line, List<Place>> , context: Context ) {
        for (line in places) {
            addMarkers(googleMap, line.value, context)
            val polyLineOption = addNewPolyline(line.value.map {
                LatLng(it.latLng!!.latitude, it.latLng.longitude)
            }, line.key.width, Color.parseColor(line.key.color))
            googleMap.addPolyline(polyLineOption).tag = line.key.name
        }
        //region poly1
        //endregion
    }
     fun places(context: Context,googleMap: GoogleMap ) {
        val places: Map<Line, List<Place>> by lazy {
            PlacesReader(context).read()
        }
//         addPolyLine(googleMap,_listOfMarkers.value.markers,context)
    }

    private fun addNewPolyline(
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
    fun getMarkers() {
        viewModelScope.launch {
            Log.e("TAG", "getMarkers ViewModel")
            val placeMarker = getMarkersUseCase.invoke().toPlace()
            _listOfMarkers.value.markers.add(placeMarker)
            val mapping = _listOfMarkers.value.markers

            mapping.forEach {  place ->
                if (mapMarkersByLine[place.line] == null){
                    mapMarkersByLine[place.line]= mutableListOf()
                }
                mapMarkersByLine[place.line]?.add(place)
            }
        }
    }

    //region  [add markers to map and call in onMapCreated]
    private fun addMarkers(googleMap: GoogleMap, listOfMarkers: List<Place>, context: Context) {
        listOfMarkers.forEach { place ->
            if (place.name != null){
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .title(place.name)
                        .position(LatLng(place.latLng!!.latitude, place.latLng.longitude))
                        .icon(Constants.trainIcon(context))
                )
            }
        }
    }

}