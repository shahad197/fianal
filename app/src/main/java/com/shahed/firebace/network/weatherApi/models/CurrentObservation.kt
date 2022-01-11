package com.shahed.firebace.network.weatherApi.models

data class CurrentObservation(
    val astronomy: Astronomy,
    val atmosphere: Atmosphere,
    val condition: Condition,
    val pubDate: Int,
    val wind: Wind
)