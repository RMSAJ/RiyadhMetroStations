package com.bootcamp.stations.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentProfileBinding
import com.bootcamp.stations.profile.model.ProfileViewModel
import com.bootcamp.stations.profile.model.ProfileViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)



        showProfile()

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.getUserInfo()
            }
        }
        binding?.apply {

            editProfile.setOnClickListener {
               val action = ProfileFragmentDirections.actionProfileFragmentToEditProfile()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProfile() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.userInfo.collect {
                    it.let {
                        binding?.profileName?.text = it.profileName
                        binding?.profilePhone?.text = it.profilePhone
                        binding?.profileEmail?.text = it.profileEmail
                        Glide.with(requireContext())
                        .load(it.profileImage.toUri())
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .placeholder(R.drawable.loading_animation)
                        .circleCrop().into(binding!!.profileImage)
                    }
                }
            }
        }
    }

}