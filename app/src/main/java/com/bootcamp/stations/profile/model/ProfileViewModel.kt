package com.bootcamp.stations.profile.model


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bootcamp.stations.DataState
import com.bootcamp.stations.LOADING_STATUS.LOADING
import com.bootcamp.stations.profile.domain.GetUserProfileUseCase
import com.bootcamp.stations.profile.domain.SetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
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
        _uiStatus.update {
            it.copy(LOADING) }
         setProfileUseCase.invoke(profileModel, uri).collect{ result->
             _uiStatus.update {
                 result}
         }
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
            }
        }
    }
}