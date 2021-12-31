package com.bootcamp.stations.user.model

data class UserModel(
    var email: String?,
    var name: String? = null,
    var imageUrl: String? = null,
    var location: GeometryLocation? = null,
    )

data class GeometryLocation(
    var lat: Double,
    var lng: Double
)
