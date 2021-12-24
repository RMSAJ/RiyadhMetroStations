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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       auth = Firebase.auth
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
//            testTest()
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
             if(username.text.toString().matches(Regex("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}"))){
                        if (pass.text.toString() == rePass.text.toString()){
//                            Test().createAccount(username.text.toString(),pass.text.toString())
                            createAccount(username.text.toString(),pass.text.toString())
                        }else{
                            rePasslay.error = "Check your password are match"
                            rePasslay.errorIconDrawable = icon
                        }
            } else{
                usernamelay.error = "Wrong Email Pattern"
                usernamelay.errorIconDrawable = icon
            }
    }

    private fun createAccount(email: String, password: String) {
//        var auth: FirebaseAuth = Firebase.auth
        binding?.btnSingupReg?.isEnabled = false
        binding?.btnSingupReg?.alpha = 0.5f
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.context, "Register SuccessFull",Toast.LENGTH_LONG).show()
                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    findNavController().navigate(action)
//                    updateUI(user)
                } else {
                    binding?.btnSingupReg?.isEnabled = true
                    binding?.btnSingupReg?.alpha = 0.5f
                    Toast.makeText(context, task.exception?.message,Toast.LENGTH_SHORT).show()
                    // If sign in fails, display a message to the user.

//                    updateUI(null)
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

} // end fragment