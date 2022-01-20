package com.shahed.firebace.network.model

import com.google.gson.annotations.SerializedName

data class Categorie(

    @SerializedName("categorie_name")
    var categorieName: String? = null,
    @SerializedName("categorieNameAr")
    var categorieNameAr: String? = null,
    @SerializedName("id")
    var id: String? = null


)