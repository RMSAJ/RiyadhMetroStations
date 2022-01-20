package com.bootcamp.stations.profile.ui

import com.bootcamp.stations.profile.model.ProfileModel
import com.bootcamp.stations.user.model.UserModel

data class ProfileUiState (val profileImage : String = "",
                           val profileName: String = "",
                           val profileEmail: String = "",
                           val profilePhone: String = "" )

