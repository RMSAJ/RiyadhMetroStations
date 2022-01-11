package com.bootcamp.stations.profile.ui

import com.bootcamp.stations.profile.model.ProfileModel
import com.bootcamp.stations.user.model.UserModel

data class ProfileUiState (val profile:Map<UserModel, ProfileModel> = mapOf())

