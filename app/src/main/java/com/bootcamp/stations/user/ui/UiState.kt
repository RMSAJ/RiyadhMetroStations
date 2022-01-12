package com.bootcamp.stations.user.ui

enum class LOADING_STATUS{LOADING,DONE,ERROR}

data class UserSignUpState(
    val loadingStatus: LOADING_STATUS = LOADING_STATUS.DONE,
    val userMsg: String = ""
)

data class UserUiState(
    val email: String = "",
    val pass: String = "",
    val repass:String = ""
)
