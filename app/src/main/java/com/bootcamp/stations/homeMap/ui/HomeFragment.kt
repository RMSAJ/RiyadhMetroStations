package com.bootcamp.stations.homeMap.ui

import com.bootcamp.stations.homeMap.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import  com.google.android.libraries.places.api.model.Place.Field.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.BuildConfig
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentHomeBinding
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
import kotlinx.android.synthetic.main.item_marker.*
import kotlinx.coroutines.launch

internal class HomeFragment : Fragment(), OnMapReadyCallback {
    //n variabls
    //entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    //entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val personIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.Cyan_700)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_profile, color)
    }
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    private var likelyPlaceNames: Array<String?> =arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> =arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> =arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> =arrayOfNulls(0)

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private val viewModel: MapViewModel by activityViewModels {
        MapViewModelFactory()
    }
    val trainIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.Primary_Green_900)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.riyadh_train, color)
    }

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
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
        (activity as? AppCompatActivity)?.setSupportActionBar(binding?.appBarSetting)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireContext())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace()
            getDeviceLocation()
        } else if (item.itemId == R.id.settings) {
            goToSettings()
        }
        return true
    }

    private fun goToSettings(){
        val action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                viewModel.mapUiState.collect {
//                    addPolyLine(it.marker)
//                    Log.d(TAG, "onMapReady: ${it.marker}")
//                }
//            }
//        }
        getLocationPermission()

    }
//endregion

    //region save the map instance location
    override fun onSaveInstanceState(outState: Bundle) {
        map.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map?.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
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
        if (ContextCompat.checkSelfPermission(this.requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    //endregion
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map!!.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation =  null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(NAME, ADDRESS, LAT_LNG)
            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result
                    map!!.isMyLocationEnabled = true
                    val count =
                        if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                            likelyPlaces.placeLikelihoods.size
                        } else {
                            M_MAX_ENTRIES
                        }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces.placeLikelihoods ?: emptyList()) {
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }
                    openPlacesDialog()
                    } else {
                        Log.e(TAG, "Exception: %s", task.exception)
                    }
                }
            } else {
                defaultMarkerOfTheUSer()
            }
        }

    private fun defaultMarkerOfTheUSer( ) {
        // The user has not granted permission.
        Log.i(TAG, "The user did not grant location permission.")
        map?.addMarker(
            MarkerOptions()
                .title((R.string.default_info_title.toString()))
                .icon(personIcon)
                .position(defaultLocation)
                .snippet((R.string.default_info_snippet.toString()))
        )

    }

    //region [START maps_current_place_open_places_dialog]
    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener =
            DialogInterface.OnClickListener{_, which->// The "which" argument contains the position of the selected item.
                val markerLatLng = likelyPlaceLatLngs[which]
                var markerSnippet = likelyPlaceAddresses[which]
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
                }
                if (markerLatLng == null) {
                    return@OnClickListener
                }
                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map?.addMarker(
                    MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet)
                )
                // Position the map's camera at the location of the marker.
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markerLatLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }
// Display the dialog.
        AlertDialog.Builder(this.requireContext())
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.mapUiState.collect {
                    addPolyLine(googleMap,it.marker)
                    Log.d(TAG, "onMapReady: ${it.marker}")
                }
            }
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        MarkerInfoWindowAdapter(this.requireContext())

        getLocationPermission()

        map!!.setOnInfoWindowClickListener {
            Log.e(TAG, "onMapReady:id : ${it.id} title : ${it.title} lng : ${it.position.longitude.toString()} ", )
            val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(title = it.title!!,id= it.id,
                lat= it.position.latitude.toString(), lng = it.position.longitude.toString() )
            findNavController().navigate(action)

        }
//        googleMap.mapType
    }

    private fun navigation(map:GoogleMap){
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

    }

    //region  [add markers to map and call in onMapCreated]
    private fun addMarkers(
        listOfMarkers: List<MarkerItemUIStatus>,
    googleMap: GoogleMap) {
        listOfMarkers.forEach { place ->
            if (!place.name.isNullOrBlank()) {
                googleMap.addMarker(
                    MarkerOptions()
                        .title(place.name)
                        .position(LatLng(place.latLng.latitude, place.latLng.longitude))
                        .icon(trainIcon)
                )
            }
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }

    private fun addPolyLine(map: GoogleMap,
        places: Map<LineUiStates, List<MarkerItemUIStatus>>) {

        for (line in places) {
            addMarkers(line.value, map)
            val polyLineOption = viewModel.addNewPolyline(line.value.map {
                LatLng(it.latLng.latitude, it.latLng.longitude)
            }, line.key.width, Color.parseColor(line.key.color))
            map.addPolyline(polyLineOption).tag = line.key.name
        }
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map = null
    }
}