package com.bootcamp.stations.homeMap.util

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place

 fun addPolyLine(googleMap: GoogleMap, places: List<Place>) {
    val polyline: Polyline = googleMap.addPolyline(
        PolylineOptions()
            .clickable(true)
            .add(
                LatLng(
                    places[0].latLng.latitude,
                    places[0].latLng.longitude
                ),
                LatLng(places[1].latLng.latitude, places[1].latLng.longitude),
                LatLng(places[2].latLng.latitude, places[2].latLng.longitude),
                LatLng(places[3].latLng.latitude, places[3].latLng.longitude),
                LatLng(places[4].latLng.latitude, places[4].latLng.longitude),
                LatLng(places[5].latLng.latitude, places[5].latLng.longitude),
                LatLng(places[6].latLng.latitude, places[6].latLng.longitude),
                LatLng(places[7].latLng.latitude, places[7].latLng.longitude),
                LatLng(places[8].latLng.latitude, places[8].latLng.longitude),
                LatLng(places[9].latLng.latitude, places[9].latLng.longitude),
                LatLng(places[10].latLng.latitude, places[10].latLng.longitude),
                LatLng(places[11].latLng.latitude, places[11].latLng.longitude),
                LatLng(places[12].latLng.latitude, places[12].latLng.longitude),
                LatLng(places[13].latLng.latitude, places[13].latLng.longitude),
                LatLng(places[14].latLng.latitude, places[14].latLng.longitude),
                LatLng(places[15].latLng.latitude, places[15].latLng.longitude),
                LatLng(places[16].latLng.latitude, places[16].latLng.longitude),
                LatLng(places[17].latLng.latitude, places[17].latLng.longitude),
                LatLng(places[18].latLng.latitude, places[18].latLng.longitude),
                LatLng(places[19].latLng.latitude, places[19].latLng.longitude),
                LatLng(places[20].latLng.latitude, places[20].latLng.longitude),
                LatLng(places[21].latLng.latitude, places[21].latLng.longitude),
                LatLng(places[22].latLng.latitude, places[22].latLng.longitude),
                LatLng(places[23].latLng.latitude, places[23].latLng.longitude),
                LatLng(places[24].latLng.latitude, places[24].latLng.longitude),
                LatLng(places[25].latLng.latitude, places[25].latLng.longitude),
                LatLng(places[25].latLng.latitude, places[25].latLng.longitude)
            )
            .color(Color.CYAN)
            .width(18f)
            .jointType(JointType.ROUND))
    polyline.tag = "Line1"
    val polyline1 : Polyline = googleMap.addPolyline(
        PolylineOptions()
            .clickable(true)
            .add(
                LatLng(places[26].latLng.latitude, places[26].latLng.longitude),
                LatLng(places[27].latLng.latitude, places[27].latLng.longitude),
                LatLng(places[28].latLng.latitude, places[28].latLng.longitude),
                LatLng(places[29].latLng.latitude, places[29].latLng.longitude),
                LatLng(places[30].latLng.latitude, places[30].latLng.longitude),
                LatLng(places[31].latLng.latitude, places[31].latLng.longitude),
                LatLng(places[32].latLng.latitude, places[32].latLng.longitude),
                LatLng(places[33].latLng.latitude, places[33].latLng.longitude),
                LatLng(places[34].latLng.latitude, places[34].latLng.longitude),
                LatLng(places[35].latLng.latitude, places[35].latLng.longitude),
                LatLng(places[36].latLng.latitude, places[36].latLng.longitude),
                LatLng(places[37].latLng.latitude, places[37].latLng.longitude),
                LatLng(places[38].latLng.latitude, places[38].latLng.longitude),
                LatLng(places[39].latLng.latitude, places[39].latLng.longitude),
                LatLng(places[40].latLng.latitude, places[40].latLng.longitude),
                LatLng(places[41].latLng.latitude, places[41].latLng.longitude),
                LatLng(places[42].latLng.latitude, places[42].latLng.longitude),
                LatLng(places[43].latLng.latitude, places[43].latLng.longitude),
                LatLng(places[44].latLng.latitude, places[44].latLng.longitude),
                LatLng(places[45].latLng.latitude, places[45].latLng.longitude),
                LatLng(places[46].latLng.latitude, places[46].latLng.longitude),
                LatLng(places[47].latLng.latitude, places[47].latLng.longitude),
                LatLng(places[48].latLng.latitude, places[48].latLng.longitude),
                LatLng(places[49].latLng.latitude, places[49].latLng.longitude),
            ) .color(Color.argb(100,255,87,34))
            .width(18f)
            .jointType(JointType.ROUND)
    )
    polyline1.tag = "Line3"

    val polyline3: Polyline = googleMap.addPolyline(
        PolylineOptions()
            .clickable(true)
            .add(
                LatLng(places[50].latLng.latitude, places[50].latLng.longitude),
                LatLng(places[51].latLng.latitude, places[51].latLng.longitude),
                LatLng(places[52].latLng.latitude, places[52].latLng.longitude),
                LatLng(places[53].latLng.latitude, places[53].latLng.longitude),
                LatLng(places[54].latLng.latitude, places[54].latLng.longitude),
                LatLng(places[55].latLng.latitude, places[55].latLng.longitude),
                LatLng(places[56].latLng.latitude, places[56].latLng.longitude),
                LatLng(places[57].latLng.latitude, places[57].latLng.longitude),
                LatLng(places[58].latLng.latitude, places[58].latLng.longitude),
                LatLng(places[59].latLng.latitude, places[59].latLng.longitude),
                LatLng(places[60].latLng.latitude, places[60].latLng.longitude),
                LatLng(places[61].latLng.latitude, places[61].latLng.longitude),
                LatLng(places[62].latLng.latitude, places[62].latLng.longitude),
                LatLng(places[63].latLng.latitude, places[63].latLng.longitude),
                LatLng(places[64].latLng.latitude, places[64].latLng.longitude),
                LatLng(places[64].latLng.latitude, places[64].latLng.longitude),


                ).width(18f)
            .color(Color.argb(100,121,0,142))
    )
    polyline3.tag = "Line6"
}