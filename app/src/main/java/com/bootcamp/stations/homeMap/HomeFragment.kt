package com.bootcamp.stations.homeMap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentHomeBinding
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.bootcamp.stations.homeMap.dataLayer.data.PlaceRenderer
import com.bootcamp.stations.homeMap.dataLayer.data.PlacesReader
import com.bootcamp.stations.user.UserViewModel
import com.bootcamp.stations.user.model.FactoryViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.google.maps.android.ktx.model.markerOptions


internal class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }
    private var locationPermissionGranted = false


    private var circle: Circle? = null

    //    private lateinit var auth: FirebaseAuth
private val places: List<Place> by lazy {
    PlacesReader(this.requireContext()).read()
}

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    private lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        auth = Firebase.auth }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        // Set up the toolbar.
        (activity as? AppCompatActivity)?.setSupportActionBar(binding?.appBar)

//        val modalBottomSheet = BottomSheetFragment()
//        modalBottomSheet.show(childFragmentManager, BottomSheetFragment.TAG)

        return binding?.root
    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            //get Map
            val googleMap = mapFragment.awaitMap()

            // Wait for map to finish loading
            googleMap.awaitMapLoad()

            // Ensure all places are visible in the map
            val bounds = LatLngBounds.builder()
            places.forEach { bounds.include(it.latLng) }
//            setMapStyle(map)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10))

            addClusteredMarkers(googleMap)
        }

//        binding?.logOut?.setOnClickListener {
//            auth.signOut()
//            val action = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
//            findNavController().navigate(action)
//        }

    }

    private fun addClusteredMarkers(googleMap: GoogleMap) {
        val clusterManager = ClusterManager<Place>(this.requireActivity(), googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this.requireActivity(),
                googleMap,
                clusterManager)
        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this.requireActivity()))
        clusterManager.markerCollection.setOnInfoWindowClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetFragment)
        }
//        setMapStyle(map)
//        enableMyLocation()
        // Add the places to the ClusterManager.
        clusterManager.addItems(places)

        clusterManager.cluster()
        mapClicks(clusterManager, googleMap)
    }

//    private fun addPolyLine(googleMap: GoogleMap, item1: List<Place>) {
//
//         val places: List<Place> by l
//
//        polyline =  googleMap.addPolyline(
//
//             PolylineOptions()
//                .clickable(true)
//                .add()
//    }

    private fun mapClicks(clusterManager: ClusterManager<Place>, googleMap:GoogleMap ){
        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
        googleMap.setOnCameraMoveStartedListener {

            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }
        googleMap.setOnCameraIdleListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            clusterManager.onCameraIdle()
        }

    }

    // new below

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }


}