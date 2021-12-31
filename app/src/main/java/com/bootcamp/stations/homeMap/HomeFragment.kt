package com.bootcamp.stations.homeMap

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
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
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad


internal class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }
    private val latitude = 24.835411021391803
    private val longitude = 46.718609606634686
    private var polyline: Polyline?  = null

    private var circle: Circle? = null

    //    private lateinit var auth: FirebaseAuth
private val places: List<Place> by lazy {
    PlacesReader(this.requireContext()).read()
}

    private val REQUEST_LOCATION_PERMISSION = 1

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
//        clusterManager
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


}