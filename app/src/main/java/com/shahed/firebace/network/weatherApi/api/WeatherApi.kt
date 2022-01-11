package com.shahed.firebace.network.weatherApi.api

import com.shahed.firebace.network.weatherApi.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {


    @GET(
        "weather?rapidapi-key=daa4e37fe3msh036b6a6cc6ff18fp183915jsn125817644824" +
                "&location=sunnyvale" +
                "&format=json" +
                "&u=f"
    )
    fun searchLocation(@Query("location") location: String): Call<WeatherResponse>


}