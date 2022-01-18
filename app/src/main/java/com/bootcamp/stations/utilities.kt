package com.bootcamp.stations

object Constants {

    const val FAVOURITE = "Favorite"
    const val PROFILE = "profile"


    const val EMAIL = "profileEmail"
    const val IMAGE1 = "https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_2.jpg?alt=media&token=52f3736d-a501-4bfd-95a7-20d4f3fff58c"
    const val IMAGE2 = "https://firebasestorage.googleapis.com/v0/b/pristine-dahlia-321713.appspot.com/o/images%2Finfo_3.jpg?alt=media&token=656ae9bd-3f2b-4112-a7a7-35673c1e372e"
    const val favMarker = "marker"

}

enum class LOADING_STATUS{LOADING,DONE,ERROR}

data class DataState(
    var loadingStatus: LOADING_STATUS = LOADING_STATUS.DONE,
    val userMsg: String = ""
)
//data class DataStatesent(
//    var loadingStatus: LOADING_STATUS = LOADING_STATUS.DONE,
//    val userMsg: String = ""
//)