package com.bootcamp.stations.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentFavoriteBinding
import com.bootcamp.stations.databinding.FragmentProfileBinding
import com.bootcamp.stations.databinding.FragmentRegisterBinding


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
//        binding?.lifecycleOwner = this
        return _binding?.root    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}