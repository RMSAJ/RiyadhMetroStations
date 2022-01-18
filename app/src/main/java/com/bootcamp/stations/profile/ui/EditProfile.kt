package com.bootcamp.stations.profile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bootcamp.stations.LOADING_STATUS
import com.bootcamp.stations.databinding.UserProfileBinding
import com.bootcamp.stations.profile.model.ProfileViewModel
import com.bootcamp.stations.profile.model.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfile : Fragment() {

    private var _binding: UserProfileBinding? = null
    private val binding get() = _binding

    private val REQUEST_CODE = 200
    private var fileImage: Uri? = null

    private val viewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = UserProfileBinding.inflate(inflater, container, false)
//        binding?.lifecycleOwner = this
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        theUiStatus()

        binding?.apply {
            settings.setOnClickListener {
                val action = EditProfileDirections.actionEditProfileToSettingsFragment()
                findNavController().navigate(action)
            }
            profileImage.setOnClickListener {
                openGalleryForImage()
            }
            save.setOnClickListener {
                setTheData()

            }
            cancelButton.setOnClickListener {
                findNavController().navigate(EditProfileDirections.actionEditProfileToProfileFragment())
            }
        }


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

    private fun setTheData() {
        viewModel.prepareTheData(
            "",
            binding?.profileName?.text.toString(),
            binding?.profilePhone?.text.toString(),
            binding?.profileEmail?.text.toString(), fileImage
        )

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
        findNavController().navigate(EditProfileDirections.actionEditProfileToProfileFragment())
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


}