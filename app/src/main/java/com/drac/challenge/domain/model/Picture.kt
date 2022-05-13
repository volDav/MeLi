package com.drac.challenge.domain.model

import com.google.gson.annotations.SerializedName

data class Picture (
    val id: String,
    val url: String,
    @SerializedName("secure_url")
    val secureUrl: String
)