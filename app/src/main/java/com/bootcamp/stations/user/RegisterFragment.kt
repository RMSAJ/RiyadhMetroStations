package com.bootcamp.stations.user

import android.graphics.drawable.Drawable
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
import com.bootcamp.stations.user.model.FactoryViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var username:TextInputEditText
    private lateinit var usernamelay:TextInputLayout
    private lateinit var passlay:TextInputLayout
    private lateinit var pass:TextInputEditText
    private lateinit var rePass:TextInputEditText
    private lateinit var rePasslay:TextInputLayout
    private lateinit var icon: Drawable

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
        username = binding!!.regUsername
        usernamelay = binding!!.regUsernameLay
        pass = binding!!.regPassword
        passlay = binding!!.regPasswordLay
        rePass = binding!!.regCnfPassword
        rePasslay = binding!!.regCnfPasswordLay
        icon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_error_outline_24
        )!!
        icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        binding?.btnLogninReg?.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        binding?.btnSingupReg?.setOnClickListener {
            checkUserIsValidToRegister()
//            testTest()
        }
//        binding?.regUsername?.setOnTextChanged { text, start, before, count ->  }
    }


    private fun checkUserIsValidToRegister() {
        binding?.btnSingupReg?.isEnabled = false

        binding?.btnSingupReg?.alpha = 0.5f
        binding?.btnSingupReg?.isEnabled = true
        viewModel.checkUserInfoEmpty(username, usernamelay, icon,pass,passlay,rePass,rePasslay)

        if(viewModel.isValidUser.value == true) {
            auth.createUserWithEmailAndPassword(username.text.toString(),pass.text.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this.requireContext(), "Registration is Completed", Toast.LENGTH_SHORT).show()
                        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(this.requireContext(),"passwords don't match or wrong email pattern", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }



//    private fun createAccount(email: String, password: String) {
////        var auth: FirebaseAuth = Firebase.auth
//        binding?.btnSingupReg?.isEnabled = false
//        binding?.btnSingupReg?.alpha = 0.5f
//        // [START create_user_with_email]
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this.context, "Register SuccessFull",Toast.LENGTH_LONG).show()
//                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
//                    findNavController().navigate(action)
//                } else {
//                    binding?.btnSingupReg?.isEnabled = true
//                    binding?.btnSingupReg?.alpha = 0.5f
//                    Toast.makeText(context, task.exception?.message,Toast.LENGTH_SHORT).show()
//                    // If sign in fails, display a message to the user.
////                    updateUI(null)
//                }
//            }
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

} // end fragment


//linked List