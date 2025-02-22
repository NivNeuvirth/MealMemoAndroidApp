package com.example.mealmemoapp.data.models

import com.google.gson.annotations.SerializedName

data class PlacesResponse(val results:List<Place>)
data class Place(val name:String,val geometry:Geometry)
data class Geometry(val location:LocationData)
data class LocationData(
    @SerializedName("lat") val lat:Double,
    @SerializedName("lng")val lng:Double
)