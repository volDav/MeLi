package com.drac.challenge.data.network.model

import com.google.gson.annotations.SerializedName

data class PictureModel (
    val id: String,
    val url: String,
    @SerializedName("secure_url")
    val secureUrl: String
)