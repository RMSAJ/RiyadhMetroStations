package com.bootcamp.stations.homeMap

import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

open class MapCallBack:OnMapReadyCallback {

    override fun onMapReady(googleMap:  GoogleMap) {
//        this.map = googleMap
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI()
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation()
//
//        map!!.setInfoWindowAdapter(MarkerInfoWindowAdapter(this.requireContext()))
//
//        map!!.setOnInfoWindowClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(it.title!!)
//            findNavController().navigate(action)
//        }
////googleMap.mapType
//        viewModel.places(this.requireContext(), googleMap)    }
    }
}
