package com.bootcamp.stations.favorite.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.databinding.FragmentFavoriteBinding
import com.bootcamp.stations.favorite.model.FavoriteViewModel
import com.bootcamp.stations.favorite.model.FavoriteViewModelFactory
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private val viewModel: FavoriteViewModel by activityViewModels{
        FavoriteViewModelFactory()
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.favoriteList.collect {

                    it.let {

                        adapter.submitList(it)

                    }
                }
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}