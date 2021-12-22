package com.bootcamp.stations.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.bootcamp.stations.R
import com.bootcamp.stations.Test
import com.bootcamp.stations.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel: UserViewModel by activityViewModels()


//   private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
            checkUSerName()
            testTest()
        }
//        binding?.regUsername?.setOnTextChanged { text, start, before, count ->  }

    }

    private fun checkUSerName() {
        val username = binding!!.regUsername
        val usernamelay = binding!!.regUsernameLay
        val pass = binding!!.regPassword
        val passlay = binding!!.regPasswordLay
        val rePass = binding!!.regCnfPassword
        val rePasslay = binding!!.regCnfPasswordLay
        val icon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_error_outline_24
        )
        icon!!.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

        viewModel.checkUserInfoEmpty(username, usernamelay, icon)
        viewModel.checkUserPassEmpty(pass, passlay, icon)
        viewModel.checkUserRePassEmpty(rePass, rePasslay, icon)


    }

    private fun testTest () {

        val username = binding!!.regUsername.text.toString()
        val pass = binding!!.regPassword.text.toString()



        Test().createAccount(username,pass)

    }

} // end fragment