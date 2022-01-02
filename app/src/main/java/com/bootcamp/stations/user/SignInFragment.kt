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
import com.bootcamp.stations.databinding.FragmentSignInBinding
import com.bootcamp.stations.user.model.FactoryViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {

private var _binding : FragmentSignInBinding? = null
private val binding get() = _binding
    private val viewModel: UserViewModel by activityViewModels {
        FactoryViewModel()
    }


private lateinit var username:TextInputEditText
private lateinit var usernameLay:TextInputLayout
private lateinit var password:TextInputEditText
private lateinit var passwordLay:TextInputLayout
private lateinit var auth:FirebaseAuth
private lateinit var icon: Drawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        username = binding!!.signIn
        password =binding!!.password
        usernameLay = binding!!.signInLay
        passwordLay =binding!!.passwordLay
        auth = Firebase.auth
         icon = AppCompatResources.getDrawable(
             requireContext(),
             R.drawable.ic_baseline_error_outline_24
         )!!
        icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)

//        val currentUser = auth.currentUser
//
//        if (currentUser != null) {
//            val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
//            findNavController().navigate(action)
//        }
        return binding?.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnSingup?.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
        binding?.btnLognin?.setOnClickListener {
            checkUSerName()
//            testTest()
        }
    }
    private fun checkUSerName(){
        viewModel.checkTheSignIn(username, usernameLay, icon,password,passwordLay)
//        viewModel.checkUserRePassEmpty(rePass, rePasslay, icon)
        if(username.text.toString().matches(Regex("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}"))){
                    fireBaseSignIn()

        } else{
            usernameLay.error = "Wrong Email Pattern"
            usernameLay.errorIconDrawable = icon
        }
    }

    private fun fireBaseSignIn(){
        binding?.btnLognin?.isEnabled = false
        binding?.btnLognin?.alpha = 0.5f
        auth.signInWithEmailAndPassword(username.text.toString(),password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this.context, "Register SuccessFull", Toast.LENGTH_LONG).show()
                    val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment(auth.uid!!)
                    findNavController().navigate(action)
                }else{
                    binding?.btnLognin?.isEnabled = true
                    binding?.btnLognin?.alpha = 0.5f
                    Toast.makeText(this.context, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}