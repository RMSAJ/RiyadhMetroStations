package com.bootcamp.stations.profile.model


import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.*
import com.bootcamp.stations.DataState
import com.bootcamp.stations.LOADING_STATUS
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

    private var _uiStatus = MutableStateFlow(DataState())
    val uiStatus: LiveData<DataState> = _uiStatus.asLiveData()

    private val _userInfo = MutableStateFlow(ProfileModel())
    val userInfo = _userInfo.asStateFlow()

    private val _readyToNavigate = MutableLiveData<Boolean>()
    val  readyToNavigate = _readyToNavigate

private fun setProfile(profileModel: ProfileModel, uri: Uri?) {
    viewModelScope.launch {
       _uiStatus.value.loadingStatus = LOADING_STATUS.LOADING
    val isthedataHasBeenSent = setProfileUseCase.invoke(profileModel, uri)
        if (isthedataHasBeenSent) {
            _uiStatus.value.loadingStatus = LOADING_STATUS.DONE
            _readyToNavigate.value = true
        }
        else _uiStatus.value.loadingStatus = LOADING_STATUS.ERROR
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
            getUserProfileUseCase.invoke().collect{
                _userInfo.value = it
                Log.d("TAG", "kkkkkkkkkkkk: ${_userInfo.value} ")
            }
        }
    }



//
}