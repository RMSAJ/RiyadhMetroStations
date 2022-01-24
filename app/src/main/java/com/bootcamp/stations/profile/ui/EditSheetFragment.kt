package com.bootcamp.stations.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bootcamp.stations.databinding.EditSheetBinding
import com.bootcamp.stations.profile.model.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditSheetFragment: BottomSheetDialogFragment() {

    private val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: EditSheetBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = EditSheetBinding.inflate(inflater,container,false)

        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}