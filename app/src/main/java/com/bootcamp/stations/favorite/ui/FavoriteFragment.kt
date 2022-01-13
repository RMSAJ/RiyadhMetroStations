package com.bootcamp.stations.favorite.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentFavoriteBinding
import com.bootcamp.stations.databinding.FragmentProfileBinding
import com.bootcamp.stations.databinding.FragmentRegisterBinding
import com.bootcamp.stations.homeMap.ui.BottomViewModel
import com.bootcamp.stations.homeMap.ui.MapViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private val viewModel: BottomViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        binding?.bottomViewModel = viewModel
        binding?.itemLinner?.adapter = FavoriteAdapter()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.settings?.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
        val adapter = FavoriteAdapter()
        binding?.itemLinner?.adapter = adapter
        viewModel.fav.observe(viewLifecycleOwner, {
            it.let {  adapter.submitList(it)

            }
        })

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}