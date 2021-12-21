package com.bootcamp.stations.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.bootcamp.stations.R
import com.bootcamp.stations.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding
    val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
//        binding?.lifecycleOwner = this

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnLogninReg?.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        binding?.btnSingupReg?.setOnClickListener {
            checkRegisterForm()
        }
//        binding?.regUsername?.setOnTextChanged { text, start, before, count ->  }
    }

     private fun checkRegisterForm(){
        val username = binding!!.regUsername
        val usernamelay = binding!!.regUsernameLay
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_error_outline_24)
        icon!!.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
       viewModel.checkUserNam(username,usernamelay, icon)
//         Toast.makeText(this.context,"heeey",Toast.LENGTH_SHORT).show()
    }
}