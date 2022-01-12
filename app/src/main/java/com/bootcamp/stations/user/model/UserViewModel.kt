package com.bootcamp.stations.user.model

import androidx.lifecycle.*
import com.bootcamp.stations.user.domain.AddUserUseCase
import com.bootcamp.stations.user.ui.LOADING_STATUS
import com.bootcamp.stations.user.ui.UserSignUpState
import com.bootcamp.stations.user.ui.UserUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val addUserUseCase: AddUserUseCase) :ViewModel() {
//    private lateinit var auth: FirebaseAuth

    private val _userId = MutableStateFlow(UserUiState())
    val userId:LiveData<UserUiState>  = _userId.asLiveData()

    private var _uiStatus =  MutableStateFlow(UserSignUpState())
     var uiStatus:LiveData<UserSignUpState> = _uiStatus.asLiveData()


    //?endregion End the com.bootcamp.stations.user.util.Validation of the Register
    fun registration(userUiState: UserUiState) {
        viewModelScope.launch {
            _uiStatus.update { it.copy(loadingStatus = LOADING_STATUS.LOADING) }


            delay(5000)

            _uiStatus.update {
                it.copy(loadingStatus = LOADING_STATUS.ERROR, "not Valid User")
            }
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

   private fun onCompleteRegistration(userModel: UserModel){
        viewModelScope.launch {
            addUserUseCase.invoke(userModel)
        }
    }




}