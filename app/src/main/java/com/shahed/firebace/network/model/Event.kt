package com.shahed.firebace.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Event(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("date")
    var date: String? = "0",
    @SerializedName("category")
    var category: String? = "0",
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("lat")
    var lat: String? = null,
    @SerializedName("lng")
    var lng: String? = null,
    @SerializedName("userID")
    var userID: String? = null,
    @SerializedName("userdetailList")
    var userdetailList: MutableList<userdetail>? = ArrayList(),
) : Serializable {
    data class userdetail(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("userName")
        var userName: String? = null,


        )
}


