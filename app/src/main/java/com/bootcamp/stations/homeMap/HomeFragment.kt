package com.bootcamp.stations.homeMap

import com.bootcamp.stations.homeMap.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import  com.google.android.libraries.places.api.model.Place.Field.*
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
import com.bootcamp.stations.homeMap.ui.MapViewModel
import com.bootcamp.stations.homeMap.ui.MapViewModelFactory
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

internal class HomeFragment : Fragment(), OnMapReadyCallback {

    //n variabls

    //entry point to the Places API.
   private lateinit var placesClient: PlacesClient
    //entry point to the Fused Location Provider.
   private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

 private lateinit  var onMapReadyCallback: MapCallBack

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private val viewModel: MapViewModel by activityViewModels{
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace(map!!,viewModel.locationPermissionGranted.value!!,placesClient,this.requireContext())
            viewModel.getTheDeviceLocation(this.requireActivity(),map!!,fusedLocationProviderClient)
            getDeviceLocation(this.requireActivity(),viewModel.locationPermissionGranted.value!!,
                map!!, viewModel.lastKnownLocation?.value!!, fusedLocationProviderClient)
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
//            onMapReadyCallback.onMapReady(map!!)
        }
        getLocationPermission()

    }
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
            viewModel.isPermitionGranted( true)
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
       getDeviceLocation(this.requireActivity(),viewModel.locationPermissionGranted.value!!,
           googleMap,viewModel.lastKnownLocation?.value!!,fusedLocationProviderClient)

       map!!.setInfoWindowAdapter(MarkerInfoWindowAdapter(this.requireContext()))

       map!!.setOnInfoWindowClickListener {
           val action = HomeFragmentDirections.actionHomeFragmentToBottomSheetFragment(it.title!!)
           findNavController().navigate(action)
       }
        googleMap.mapType
      viewModel.places(this.requireContext(), googleMap)


    }

}