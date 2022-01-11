package com.bootcamp.stations.user.model

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.user.domain.AddUserUseCase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class UserViewModel(private val addUserUseCase: AddUserUseCase) :ViewModel() {
//    private lateinit var auth: FirebaseAuth

    private val _userId = MutableLiveData<String>()
    val userId:LiveData<String>  = _userId
    private var _isValidUser =  MutableLiveData<Boolean>()
     var isValidUser =  _isValidUser

//    private lateinit var auth: FirebaseAuth
//    private lateinit var fireBase: Firebase


    //region  The Emptiness com.bootcamp.stations.user.util.Validation of the Register
    fun checkUserInfoEmpty(userEmailInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable,
                           userPassInfo: TextInputEditText, userPassLay: TextInputLayout,
                           userRePassInfo: TextInputEditText, userRePassLay: TextInputLayout) {
         if (userEmailInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the UserName"
            userNameLay.errorIconDrawable = icon

        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            checkUserPassEmpty(userEmailInfo,userPassInfo,userPassLay,icon,userRePassInfo,userRePassLay)
        }
    }

    private fun checkUserPassEmpty(userEmailInfo: TextInputEditText,userPassInfo: TextInputEditText, userPassLay: TextInputLayout, icon: Drawable,
                                   userRePassInfo: TextInputEditText, userRePassLay: TextInputLayout) {
         if (userPassInfo.text.toString().isBlank() ) {
             userPassLay.error = "Please Type the Password"
             userPassLay.errorIconDrawable = icon

        } else {
             userPassLay.error = null
             userPassLay.errorIconDrawable = null
            checkUserRePassEmpty(userEmailInfo,userPassInfo,userRePassInfo,userRePassLay,icon)

        }
    }

    private fun checkUserRePassEmpty(userEmailInfo: TextInputEditText,userPassInfo:TextInputEditText,userRePassInfo: TextInputEditText, userRePassLay: TextInputLayout, icon: Drawable ) {
         if (userRePassInfo.text.toString().isBlank() ) {
             userRePassLay.error = "Please Type the re-Type password"
             userRePassLay.errorIconDrawable = icon
        } else {
             userRePassLay.error = null
             userRePassLay.errorIconDrawable = null
            isValidEmailAndPass(userEmailInfo,userPassInfo,userRePassInfo)
        }
    }
    //?endregion End the com.bootcamp.stations.user.util.Validation of the Register

    //region email and pass validation of the Register
    private fun isValidEmailAndPass(userEmail:TextInputEditText, userPassInfo:TextInputEditText,
                                    userRePassInfo: TextInputEditText) {
        if (userEmail.text.toString()
                .matches(Regex("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}"))
        ) {
            if (userPassInfo.text.toString() == userPassInfo.text.toString()) {
                userIsValidToCreateAccount( userEmail.text.toString())
                _isValidUser.value = true
            } else {
                userPassInfo.error = "Check your password are match"
                userRePassInfo.error = "Check your password are match"
            }
        } else {
            userEmail.error = "Wrong Email Pattern"
            _isValidUser.value = false

        }
    }
    //?endregion


    //on complete Registration add-To FireBase_Authentication
    private fun prepareDataToInser(email: String): UserModel {
    return UserModel(email = email)
    }

    private fun userIsValidToCreateAccount(email: String){
       val prepareData = prepareDataToInser(email)
        onCompleteRegistration(prepareData)
    }



//    fun additemToUser(){
//        val userName:String
//        val userId:Int
//    }

   private fun onCompleteRegistration(userModel: UserModel){
        viewModelScope.launch {
            addUserUseCase.invoke(userModel)
        }
    }

    fun checkTheSignIn(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable,
                       userPassInfo: TextInputEditText, userPassLay: TextInputLayout): Boolean {
        return if (userInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the re-Type password"
            userNameLay.errorIconDrawable = icon
            false
        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            checkTheSignInPass(userPassInfo,userPassLay,icon)
            true
        }
    }

    private fun checkTheSignInPass(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable): Boolean{
        return if (userInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the re-Type password"
            userNameLay.errorIconDrawable = icon
            false
        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            isSignInValid(userInfo,userNameLay,icon)
            true
        }
    }
    private fun isSignInValid(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable): Boolean{
        return  if(userInfo.text.toString().matches(Regex("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}"))) {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            false
        } else {
            userNameLay.error = "user information is invalid"
            userNameLay.errorIconDrawable = icon
            true
        }
    }


    fun onRegisterSuccess(userId:String){
        _userId.value = userId
    }
}