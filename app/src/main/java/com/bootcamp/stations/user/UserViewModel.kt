package com.bootcamp.stations.user

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UserViewModel:ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId:LiveData<String>  = _userId

    fun checkUserInfoEmpty(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable ):Boolean {
        return if (userInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the UserName"
            userNameLay.errorIconDrawable = icon
            false
        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            true
        }
    }

    fun checkUserPassEmpty(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable ):Boolean {
        return if (userInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the Password"
            userNameLay.errorIconDrawable = icon
            false
        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            true
        }
    }

    fun checkUserRePassEmpty(userInfo: TextInputEditText, userNameLay: TextInputLayout, icon: Drawable ):Boolean {
        return if (userInfo.text.toString().isBlank() ) {
            userNameLay.error = "Please Type the re-Type password"
            userNameLay.errorIconDrawable = icon
            false
        } else {
            userNameLay.error = null
            userNameLay.errorIconDrawable = null
            true
        }
    }

    fun onRegisterSuccess(userId:String){
        _userId.value = userId
    }
}