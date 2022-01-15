package com.bootcamp.stations.profile.model


import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.profile.domain.GetUserProfileUseCase
import com.bootcamp.stations.profile.domain.SetProfileUseCase
import com.bootcamp.stations.profile.ui.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch


class ProfileViewModel(private val setProfileUseCase: SetProfileUseCase,
private val getUserProfileUseCase: GetUserProfileUseCase) : ViewModel() {


private fun setProfile(profileModel: ProfileModel, uri: Uri?) {
    viewModelScope.launch {
    setProfileUseCase.invoke(profileModel, uri)

    }
}

init {
    getUserInfo()
}

    fun prepareTheData(image:String ,name:String, phone:String, email:String, uri: Uri? ){
        val profile = ProfileModel(image, name,email,phone )
        setProfile(profile,uri)
    }

    private fun getUserInfo(){
        viewModelScope.launch {
            getUserProfileUseCase.invoke().collect{ user ->
                _userInfo.update {
                    user.copy(profileEmail = user.profileEmail, profileName = user.profileName, profilePhone = user.profilePhone, profileImage = user.profileImage)
                }
                Log.d("TAG", "kkkkkkkkkkkk: ${user.profileName} ")
            }
        }
    }


    private val _userInfo = MutableStateFlow(ProfileModel())
    val userInfo = _userInfo.asLiveData()


//
}