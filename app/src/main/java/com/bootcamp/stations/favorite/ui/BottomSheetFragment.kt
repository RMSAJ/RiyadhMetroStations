package com.bootcamp.stations.favorite.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentBottomSheetBinding
import com.bootcamp.stations.favorite.model.BottomSheetViewModel
import com.bootcamp.stations.favorite.model.FavoriteViewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null

    private val binding get() = _binding

    private val navigationArgs: BottomSheetFragmentArgs by navArgs()

    private val markerId by lazy { navigationArgs.id }
    private val markerTitle by lazy { navigationArgs.title }
    private val markerLocation: LatLng by lazy { LatLng(navigationArgs.lat.toDouble(), navigationArgs.lng.toDouble()) }
    private val markerLat: Double by lazy { navigationArgs.lat.toDouble() }
    private val markerLong: Double by lazy { navigationArgs.lng.toDouble() }


    private val viewModel: BottomSheetViewModel by activityViewModels{
        FavoriteViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentBottomSheetBinding.inflate(layoutInflater, container, false)



        if (true)  {
            binding?.favoriteImage?.setImageResource(R.drawable.ic_favorite)
        } else{

            binding?.favoriteImage?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


        return binding?.root
    }
//    object counter{
//        var i = 0
//        get() = field++
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTheMarkerDetails(markerTitle)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.getFavList()
            }
        }
        viewModel.favoriteList.observe(viewLifecycleOwner,{ favList ->
            val x = favList.find { it.title == navigationArgs.title }
            if (x?.title != null ) {
                binding?.favoriteImage?.setImageResource(R.drawable.ic_favorite)
            }else{
                binding?.favoriteImage?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            binding?.apply {
                favoriteCard.setOnClickListener {
                    if (x?.title != null){
                        binding?.favoriteImage?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                       viewModel.removeFav(markerId, markerTitle, markerLocation)
                    }else {
                        addToFav(markerId, markerTitle, markerLocation)
                        binding?.favoriteImage?.setImageResource(R.drawable.ic_favorite)

                    }
                }
                navigateCard.setOnClickListener {
                    navigateOnSelected(markerLocation)
                }

            }
        })

    }

    private fun addToFav(markerId: String, title: String, location: LatLng) {
        viewModel.addToFavorite(markerId,title,location)

}

    fun setTheMarkerDetails(markerTitle: String) {
        binding?.nameText?.text = markerTitle
    }

    private fun navigateOnSelected(markerLocation: LatLng) {
        val getMovenUri =
        Uri.parse("geo:0,0?q=${markerLat},${markerLong}")
        val mapIntent = Intent(Intent.ACTION_VIEW, getMovenUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}