package com.bootcamp.stations.homeMap

import com.bootcamp.stations.homeMap.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import  com.google.android.libraries.places.api.model.Place.Field.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.BuildConfig
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentHomeBinding
import com.bootcamp.stations.homeMap.dataLayer.data.Line
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.bootcamp.stations.homeMap.ui.LineUiStates
import com.bootcamp.stations.homeMap.ui.MapViewModel
import com.bootcamp.stations.homeMap.ui.MapViewModelFactory
import com.bootcamp.stations.homeMap.ui.MarkerItemUIStatus
import com.bootcamp.stations.homeMap.util.Constants.personIcon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import kotlinx.coroutines.launch

internal class HomeFragment : Fragment(), OnMapReadyCallback {

    //n variabls

    //entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    //entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private  var onMapReadyCallback: MapCallBack? = null

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private val viewModel: MapViewModel by activityViewModels {
        MapViewModelFactory()
    }

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.lastKnownLocation?.value = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)

        placesClient = Places.createClient(requireContext())
        // Set up the toolbar.
        (activity as? AppCompatActivity)?.setSupportActionBar(binding?.appBar)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace()
            viewModel.getTheDeviceLocation(
                this.requireActivity(),
                map!!,
                fusedLocationProviderClient
            )
            getDeviceLocation(
                this.requireActivity(), viewModel.locationPermissionGranted.value!!,
                map!!, viewModel.lastKnownLocation?.value!!, fusedLocationProviderClient
            )
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        lifecycleScope.launchWhenCreated {

            //get Map
            mapFragment.awaitMap()
            // Wait for map to finish loading
            map?.awaitMapLoad()
           onMapReadyCallback?.onMapReady(map!!)
        }
        getLocationPermission()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.mapUiState.collect {
              //      addPolyLine(googleMap, it.marker, requireActivity())
                    Log.d(TAG, "onMapReady: ${it.marker}")
                }
            }
        }    }
//endregion

    //region save the map instance location
    override fun onSaveInstanceState(outState: Bundle) {
        map.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, viewModel.lastKnownLocation?.value)
        }
        super.onSaveInstanceState(outState)
    }
    //endregion

    //region checks whether the user has granted fine location permission. If not, it requests the permission

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.isPermitionGranted(true)
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        viewModel.isPermitionGranted(false)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    viewModel.isPermitionGranted(true)
                }
            }
        }
        updateLocationUI()
    }

    //endregion
    //region If the user has granted location permission, enable the My Location layer and the related control on the map
    private fun updateLocationUI() {
        try {
            if (viewModel.locationPermissionGranted.value!!) {
                map!!.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                viewModel.setLastKnownLocation(null)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    fun getDeviceLocation(context: FragmentActivity, locationPermissionGranted:Boolean,
                          googleMap: GoogleMap, lastKnownLocation: Location,
                          fusedLocationProviderClient: FusedLocationProviderClient
    ) {

        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(context) {
                    if (it.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation.set( it.result)
                        if (lastKnownLocation != null) {
                            googleMap.isMyLocationEnabled = true
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation.latitude,
                                        lastKnownLocation.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", it.exception)
                        googleMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        googleMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    fun showCurrentPlace(googleMap: GoogleMap) {
        if (googleMap == null) {
            return
        }
        if (viewModel.locationPermissionGranted.value!!) {
            // Use fields to define the data types to return.
            val placeFields = listOf(
                NAME,
                ADDRESS,
                LAT_LNG
            )
            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                   val likelyPlaces = task.result
                    googleMap.isMyLocationEnabled = true

                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            defaultMarkerOfTheUSer(googleMap )
        }
    }
    private fun defaultMarkerOfTheUSer(googleMap: GoogleMap ) {
        // The user has not granted permission.
        Log.i(TAG, "The user did not grant location permission.")
        googleMap.addMarker(
            MarkerOptions()
                .title((R.string.default_info_title.toString()))
                .icon(personIcon(this.requireContext()))
                .position(defaultLocation)
                .snippet((R.string.default_info_snippet.toString()))
        )

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation(
            this.requireActivity(), viewModel.locationPermissionGranted.value!!,
            googleMap, viewModel.lastKnownLocation?.value!!, fusedLocationProviderClient
        )

        map!!.setInfoWindowAdapter(MarkerInfoWindowAdapter(this.requireContext()))

        map!!.setOnInfoWindowClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(it.title!!)
            findNavController().navigate(action)
        }
//        googleMap.mapType
    }


    //region  [add markers to map and call in onMapCreated]
    private fun addMarkers(
        googleMap: GoogleMap,
        listOfMarkers: List<MarkerItemUIStatus>,
        context: Context
    ) {
        listOfMarkers.forEach { place ->
            if (place.name != null) {
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .title(place.name)
                        .position(LatLng(place.latLng.latitude, place.latLng.longitude))
                        .icon(Constants.trainIcon(context))
                )
            }
        }
    }


    private fun addPolyLine(
        googleMap: GoogleMap,
        places: Map<LineUiStates, List<MarkerItemUIStatus>>,
        context: Context
    ) {
        for (line in places) {
            addMarkers(googleMap, line.value, context)
            val polyLineOption = viewModel.addNewPolyline(line.value.map {
                LatLng(it.latLng.latitude, it.latLng.longitude)
            }, line.key.width, Color.parseColor(line.key.color))
            googleMap.addPolyline(polyLineOption).tag = line.key.name
        }
        //region poly1
        //endregion
    }
}