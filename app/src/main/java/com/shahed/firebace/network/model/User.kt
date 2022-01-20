package com.shahed.firebace.network.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_name")
    var userName: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("userType")
    var userType: String? = null,
    @SerializedName("Address")
    var Address: String? = null,
    @SerializedName("events")
    var events: MutableList<Event>? = ArrayList(),
)
