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
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

class ProfileViewModel(private val setProfileUseCase: SetProfileUseCase,
private val getUserProfileUseCase: GetUserProfileUseCase) : ViewModel() {

    private var _uiStatus = MutableStateFlow(DataState())
    val uiStatus: LiveData<DataState> = _uiStatus.asLiveData()

    private val _userInfo = MutableStateFlow(ProfileModel())
    val userInfo = _userInfo.asStateFlow()



private fun setProfile(profileModel: ProfileModel, uri: Uri?) {
    viewModelScope.launch {
        _uiStatus.update { it.copy(LOADING) }
         setProfileUseCase.invoke(profileModel, uri).collect{ result->


             _uiStatus.update {
                 result}

         }

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