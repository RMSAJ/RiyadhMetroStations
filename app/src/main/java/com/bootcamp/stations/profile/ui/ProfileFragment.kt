package com.bootcamp.stations.profile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
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

    private val REQUEST_CODE = 200
    private var fileImage: Uri? = null

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

            imgProfile.setOnClickListener {
                openGalleryForImage()
            }

            profileName.setOnClickListener {
                val name = binding?.profileName?.text.toString()
               val action = ProfileFragmentDirections.actionProfileFragmentToEditSheetFragment(name)
                findNavController().navigate(action)
            }

            emailArrow.setOnClickListener {
                val email = binding?.profileEmail?.text.toString()
                val action = ProfileFragmentDirections.actionProfileFragmentToEditSheetFragment(email)
                findNavController().navigate(action)
            }

            phoneArrow.setOnClickListener {
                val phone = binding?.profilePhone?.text.toString()
                val action = ProfileFragmentDirections.actionProfileFragmentToEditSheetFragment(phone)
                findNavController().navigate(action)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            binding?.profileImage?.setImageURI(data?.data) // handle chosen image
            fileImage = data?.data!!
        }
    }

    private fun editClickedInfo(name:String) {
        val action = ProfileFragmentDirections.actionProfileFragmentToEditSheetFragment(name)
        findNavController().navigate(action)
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