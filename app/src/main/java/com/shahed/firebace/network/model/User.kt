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
    @SerializedName("imageUrl")
    var events: MutableList<Event>? = ArrayList(),
)
