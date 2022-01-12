package com.bootcamp.stations.homeMap.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentBottomSheetBinding
import com.bootcamp.stations.homeMap.ui.BottomViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding

    private val navigationArgs: BottomSheetFragmentArgs by navArgs()

    private val viewModel: BottomViewModel by activityViewModels()

//    var fav_Name :String? = ""
//    var fav_latLng: LatLng? = (12.0566,24.565)
//    var fav_address: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(requireContext(), "onCreate ", Toast.LENGTH_SHORT).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val markerId = navigationArgs.id
        val markerTitle = navigationArgs
        val markerLocation: LatLng =
            LatLng(navigationArgs.lat.toDouble(), navigationArgs.lng.toDouble())


        binding?.favoriteCard?.setOnClickListener {
           // it.background.current
           // addToFav(markerId,markerTitle,markerLocation)
            // to move item to the list of fav
            binding?.favoriteImage?.setImageResource(R.drawable.ic_favorite)

//           viewModel.newFav(fav_Name,fav_latLng,fav_address)
            Toast.makeText(this.requireContext(), "Station save it ", Toast.LENGTH_SHORT).show()

        }

        binding?.favoriteImage?.setOnClickListener {
            Log.e("TAG", "favoriteImage: favoriteImage")
            Toast.makeText(this.requireContext(), "favoriteImage ", Toast.LENGTH_SHORT).show()
        }


    }

    private fun addToFav(markerId: String, title: String, location: LatLng) {

    }

}