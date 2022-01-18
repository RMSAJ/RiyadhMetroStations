package com.bootcamp.stations.homeMap.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bootcamp.stations.R
import com.bootcamp.stations.homeMap.dataLayer.data.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter ( private val context: Context
) : GoogleMap.InfoWindowAdapter {
    @SuppressLint("SetTextI18n")
    override fun getInfoContents(marker: Marker): View? {
        // 1. Get tag
        val place = marker.tag as? Place ?: return null

        // 2. Inflate view and set title, address, and rating
        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_info_contents, null
        )
        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = place.name
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = place.address
        view.findViewById<TextView>(
            R.id.text_view_rating
        ).text = "Rating: %.2f".format(place.rating)

        return view
    }

    // get flow branch strategy

    override fun getInfoWindow(p0: Marker): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }


}