package com.neighbor.neighborsrefrigerator.utilities

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.neighbor.neighborsrefrigerator.data.CoordinateData

class UseGeocoder() {
    private var geocoder: Geocoder = Geocoder(App.context())
    lateinit var list:List<Address>
    private var city: String = ""
    private var country:String = ""
    private var coordinate:CoordinateData = CoordinateData(0.0, 0.0)

    fun addressToLatLng(address:String): CoordinateData {
        list = geocoder.getFromLocationName(address, 10)
        if(list != null){
            city = ""
            country = ""
            if(list.isEmpty()){
                Log.d("실패", "")
            } else {
                var address:Address = list[0]
                Log.d("좌표", address.toString())
                coordinate.latitude = address.latitude
                coordinate.longitude = address.longitude
            }
        }
        return coordinate
    }
}