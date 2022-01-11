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
    @SerializedName("rateList")
    var rateList: MutableList<Rate>? = ArrayList(),
) : Serializable {
    data class Rate(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("userName")
        var userName: String? = null,
        @SerializedName("comment")
        var comment: String? = null,
        @SerializedName("rating")
        var rating: String? = "0",
    )
}


