package com.bootcamp.stations.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.stations.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UserViewModel:ViewModel() {
    private val _userEmail = MutableLiveData<String>()
    val userEmail:LiveData<String>  = _userEmail

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


}