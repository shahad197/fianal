package com.shahed.firebace.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("userID")
    var userID: String? = null,
    @SerializedName("date")
    var date: String? = "0",
    @SerializedName("eventID")
    var eventID: String? = "0",
    @SerializedName("barcode")
    var barcode: String? = null,
    @SerializedName("eventName")
    var eventName: String? = null,
    @SerializedName("eventDate")
    var eventDate: String? = null,


    ) : Serializable