package com.shahed.firebace.network.weatherApi.repo

import android.util.Log
import com.shahed.firebace.network.weatherApi.api.WeatherApi
import com.shahed.firebace.network.weatherApi.models.WeatherResponse
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherRepo"

class WeatherRepo {

    // json
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://yahoo-weather5.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()


    private val api = retrofit.create(WeatherApi::class.java)

    suspend fun searchLocation(location: String): WeatherResponse {

        val response = api.searchLocation(location).awaitResponse()
        var weatherResponse = WeatherResponse()

        if (response.isSuccessful) {

            weatherResponse = response.body()!!
            Log.e(TAG, "searchLocation: good")
        } else {
            Log.e(TAG, "searchLocation: ${response.raw()}")
        }

        return weatherResponse
    }


}