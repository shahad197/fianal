package com.shahed.firebace.network.weatherApi.models

data class Forecast(
    val code: Int,
    val date: Int,
    val day: String,
    val high: Int,
    val low: Int,
    val text: String
)