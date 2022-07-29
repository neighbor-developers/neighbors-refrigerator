package com.neighbor.neighborsrefrigerator.utilities

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class UseGeocoder() {
    private var geocoder: Geocoder = Geocoder(App.context())
    lateinit var list:List<Address>
    private var city: String = ""
    private var country:String = ""


    fun addressToLatLng(address:String): LatLng {
        var coord = LatLng(0.0, 0.0)

        list = geocoder.getFromLocationName(address, 10)
        if(list != null){
            city = ""
            country = ""
            if(list.isEmpty()){
                Log.d("실패", "")
            } else {
                var address:Address = list[0]
                Log.d("좌표", address.toString())
                coord  = LatLng(address.latitude, address.longitude)
            }
        }
        return coord
    }
}