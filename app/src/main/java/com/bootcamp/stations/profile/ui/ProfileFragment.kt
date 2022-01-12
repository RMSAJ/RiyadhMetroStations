package com.bootcamp.stations.profile.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {


    private val viewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("TAG", "onCreate: ${Firebase.auth.currentUser?.email}", )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//       viewModel.userInfo. (viewLifecycleOwner, {binding!!.textView.setText(it) })
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getUserInfo()
        Log.d("TAG", "onViewCreated: ${viewModel.userInfo.value.profileName} ")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED)

            {
                val data = viewModel.userInfo.value
                binding?.profileName?.setText(data.profileName)
                binding?.phone?.setText(data.profilePhone)
                binding?.profileEmail?.setText(data.profileEmail)
                Glide.with(requireContext()).load(data.profileImage.toUri()).error(R.drawable.ic_profile).into(binding!!.profileImage)
            }
        }

        binding?.apply {
            settings.setOnClickListener { val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
                findNavController().navigate(action) }

            editProfile.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfile()
                findNavController().navigate(action)
            }
        }
//       viewModel.name.observe(viewLifecycleOwner, {binding!!.textView.setText(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}