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
import com.bootcamp.stations.LOADING_STATUS
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
        viewModel.getUserInfo()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getUserInfo()
        Log.d("TAG", "onViewCreated: ${viewModel.userInfo.value.profileName} ")
//        theUiStatus()
        lifecycleScope.launch {
//           repeatOnLifecycle(Lifecycle.State.RESUMED)
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userInfo.collect {
                    it.let {
                        binding?.profileName?.setText(it.profileName)
                        binding?.profilePhone?.setText(it.profilePhone)
                        binding?.profileEmail?.setText(it.profileEmail)
                        Glide.with(requireContext()).load(it.profileImage.toUri())
                            .error(R.drawable.ic_profile).into(binding!!.profileImage)
                    }
                }
            }
        }
//
        binding?.apply {

            editProfile.setOnClickListener {
//                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfile()
                findNavController().navigate(R.id.editProfile)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun theUiStatus() {
        viewModel.uiStatus.observe(viewLifecycleOwner, { uiState ->
            when (uiState.loadingStatus) {
                LOADING_STATUS.LOADING -> {
                    showLoading()
                }
                LOADING_STATUS.ERROR -> {
                    showError()
                }
                LOADING_STATUS.DONE -> {
                    showContent()

                }
            }
        })
    }

    private fun showLoading() {
        binding?.contentScreen?.visibility = View.GONE
        binding?.loadingScreen?.visibility = View.VISIBLE
        binding?.errorScreen?.visibility = View.GONE
    }

    private fun showError() {
        binding?.contentScreen?.visibility = View.GONE
        binding?.loadingScreen?.visibility = View.GONE
        binding?.errorScreen?.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding?.contentScreen?.visibility = View.VISIBLE
        binding?.loadingScreen?.visibility = View.GONE
        binding?.errorScreen?.visibility = View.GONE
    }



}