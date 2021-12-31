package com.bootcamp.stations.homeMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentBottomSheetBinding
import com.bootcamp.stations.homeMap.dataLayer.BottomViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFragment :  BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding

    private val viewModel: BottomViewModel by activityViewModels()

    var fav_Name :String? = ""
//    var fav_latLng: LatLng? = (12.0566,24.565)
//    var fav_address: String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            fav_Name = it!!.getString("name")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.favoriteCard?.setOnClickListener {
            // to move item to the list of fav

//           viewModel.newFav(fav_Name,fav_latLng,fav_address)
            Toast.makeText(this.requireContext(), "Station save it ", Toast.LENGTH_SHORT).show()

        }
    }

}