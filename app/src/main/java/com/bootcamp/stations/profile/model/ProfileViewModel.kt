package com.bootcamp.stations.profile.model


import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.*
import com.bootcamp.stations.DataState
import com.bootcamp.stations.LOADING_STATUS
import com.bootcamp.stations.LOADING_STATUS.*
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



private fun setProfile(profileModel: ProfileModel, uri: Uri?) {
    viewModelScope.launch {
        val onSucsses = setProfileUseCase.invoke(profileModel, uri)
        _uiStatus.update { it.copy(onSucsses.loadingStatus) }

//        when(onSucsses.loadingStatus) {
//
//            DONE -> _uiStatus.value.loadingStatus = DONE
//            ERROR -> _uiStatus.value.loadingStatus = ERROR
//            LOADING -> _uiStatus.value.loadingStatus = LOADING
//        }
    }



}
    fun prepareTheData(image:String ,name:String, phone:String, email:String, uri: Uri? ){

        val profile = ProfileModel(image, name,email,phone )

        setProfile(profile,uri)
    }

     fun getUserInfo(){
        viewModelScope.launch {
            getUserProfileUseCase.invoke().collect{
                _userInfo.value = it
                Log.d("TAG", "kkkkkkkkkkkk: ${_userInfo.value} ")
            }
        }
    }

//
}