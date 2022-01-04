package com.bootcamp.stations.homeMap
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
import  com.google.android.libraries.places.api.model.Place.Field.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.BuildConfig
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentHomeBinding
import com.bootcamp.stations.homeMap.dataLayer.data.Line
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.bootcamp.stations.homeMap.dataLayer.data.PlacesReader
import com.bootcamp.stations.homeMap.util.BitmapHelper
import com.bootcamp.stations.user.UserViewModel
import com.bootcamp.stations.user.model.FactoryViewModel
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



internal class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

   private val COLOR_BLACK_ARGB = -0x1000000
   private val POLYLINE_STROKE_WIDTH_PX = 12
    //n variabls
   private val defaultLocation = LatLng(24.582133783959872, 46.76407041289462)
    //entry point to the Places API.
   private lateinit var placesClient: PlacesClient
    //entry point to the Fused Location Provider.
   private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
   private var locationPermissionGranted = false
   private var lastKnownLocation: Location? = null
   private var circle: Circle? = null
   private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
   private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
   private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
   private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }

    //    private lateinit var auth: FirebaseAuth
    private val places: Map<Line, List<Place>> by lazy {
        PlacesReader(this.requireContext()).read()
    }

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    private val trainIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this.requireContext(), R.color.Primary_Green_900)
        BitmapHelper.vectorToBitmap(this.requireContext(), R.drawable.train, color)
    }

    private val personIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this.requireContext(), R.color.Cyan_700)
        BitmapHelper.vectorToBitmap(this.requireContext(), R.drawable.ic_profile, color)
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        setHasOptionsMenu(true)

//        auth = Firebase.auth }
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

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
            mapFragment.getMapAsync(this@HomeFragment)
        }
    }


    //region  [add markers to map and call in onMapCreated]
    private fun addMarkers(googleMap: GoogleMap, listOfPoint: List<Place>) {
        listOfPoint.forEach { place ->
            if (place.name != null){
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(LatLng(place.latLng.latitude, place.latLng.longitude))
                    .icon(trainIcon)
            )}
        }
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

    private fun addPolyLine(googleMap: GoogleMap) {

        for (line in places) {
            addMarkers(googleMap, line.value)
            val polyLineOption = addNewPolyline(line.value.map {
                LatLng(it.latLng.latitude, it.latLng.longitude)
            }, line.key.width, Color.parseColor(line.key.color))
            googleMap.addPolyline(polyLineOption).tag = line.key.name
        }
        //region poly1
        //endregion
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
            locationPermissionGranted = true
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

    //region If the user has granted location permission, enable the My Location layer and the related control on the map
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                map!!.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    //endregion


    //region Use the fused location provider to find the device's last-known location,
    // then use that location to position the map.
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.isMyLocationEnabled = true
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    //endregion

    //region [START maps_current_place_show_current_place]
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(
               NAME,
               ADDRESS,
               LAT_LNG)
            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result
                    map!!.isMyLocationEnabled = true
                    // Set the count, handling cases where less than 5 entries are returned.
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
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }
                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            map?.addMarker(
                MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .icon(personIcon)
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet))
            )
            // Prompt the user for permission.
            getLocationPermission()
        }
    }
    //endregion [END maps_current_place_show_current_place]

    //region [START maps_current_place_open_places_dialog]
    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener =
            DialogInterface.OnClickListener { _, which -> // The "which" argument contains the position of the selected item.
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
    //endregion

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map = null
    }

    //region companion object
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
//endregion

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        map!!.setInfoWindowAdapter(MarkerInfoWindowAdapter(this.requireContext()))

        map!!.setOnInfoWindowClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(it.title!!)
            findNavController().navigate(action)
        }
//googleMap.mapType
        addPolyLine(googleMap)

        googleMap.setOnPolylineClickListener(this)
//        googleMap.setOnMyLocationButtonClickListener(this)
//        googleMap.setOnMyLocationClickListener(this)
    }

    override fun onPolylineClick(p0: Polyline) {

    }


}