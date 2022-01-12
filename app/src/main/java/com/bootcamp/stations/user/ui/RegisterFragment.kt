package com.bootcamp.stations.user.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.bootcamp.stations.databinding.FragmentRegisterBinding
import com.bootcamp.stations.user.model.FactoryViewModel
import com.bootcamp.stations.user.model.UserViewModel
import com.bootcamp.stations.user.util.InputTypes
import com.bootcamp.stations.user.util.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }

    private lateinit var auth: FirebaseAuth

//    private lateinit var fireBase:Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      auth = Firebase.auth

//        fireBase.firestore
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
        viewModel.uiStatus.observe(viewLifecycleOwner, {uiState ->
            when(uiState.loadingStatus){
                LOADING_STATUS.LOADING -> showLoading()
                LOADING_STATUS.DONE -> showContent()
                LOADING_STATUS.ERROR -> showError(uiState.userMsg)
            }
        })

        binding?.apply {

            btnLogninReg.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToSignInFragment()
                findNavController().navigate(action)
            }
            btnSingupReg.setOnClickListener {
//                checkUserIsValidToRegister()
            viewModel.registration(createUserUiState())
            }
        }

//        binding?.regUsername?.setOnTextChanged { text, start, before, count ->  }
    }


    private fun checkUserIsValidToRegister() {
        binding?.btnSingupReg?.isEnabled = false

        binding?.btnSingupReg?.alpha = 0.5f
        binding?.btnSingupReg?.isEnabled = true
//        viewModel.checkUserInfoEmpty(username, usernamelay, icon,pass,passlay,rePass,rePasslay)

//        if(viewModel.isValidUser.value == true) {
//            auth.createUserWithEmailAndPassword(username.text.toString(),pass.text.toString())
//                .addOnCompleteListener() { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this.requireContext(), "Registration is Completed", Toast.LENGTH_SHORT).show()
//                        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(auth.currentUser!!.uid)
//                        findNavController().navigate(action)
//                    } else {
//                        Toast.makeText(this.requireContext(),"passwords don't match or wrong email pattern", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//
//        }
    }

    private fun createUserUiState(): UserUiState {
        return UserUiState(
        binding?.regUsername?.text.toString(),
        binding?.regPassword?.text.toString(),
        binding?.regCnfPassword?.text.toString()
        )
    }

    private fun showError(message: String) {
        binding?.ErrorScreen?.visibility = View.VISIBLE
        binding?.errorMessage?.text = message
        binding?.ContentScreen?.visibility = View.GONE
        binding?.loadingScreen?.visibility = View.GONE
    }

    private fun showContent(){
        binding?.ErrorScreen?.visibility = View.GONE
        binding?.ContentScreen?.visibility = View.VISIBLE
        binding?.loadingScreen?.visibility = View.GONE
    }
    private fun showLoading(){
        binding?.ErrorScreen?.visibility = View.GONE
        binding?.ContentScreen?.visibility = View.GONE
        binding?.loadingScreen?.visibility = View.VISIBLE
    }
    private fun validationCheck(): Boolean {
        var isValid = true
        if (binding?.regUsernameLay!!.Validation(binding!!.regUsername, InputTypes.EMAIL)) {
            isValid = false
        }
        if (binding?.regCnfPasswordLay!!.Validation(binding!!.regPassword,InputTypes.PASSWORD )){
            isValid = false
        }
        if (binding?.regCnfPasswordLay!!.Validation(binding!!.regCnfPassword, InputTypes.REPASSWORD)){
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

} // end fragment


//linked List