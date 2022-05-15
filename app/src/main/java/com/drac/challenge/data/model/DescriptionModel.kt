package com.drac.challenge.data.model

import com.google.gson.annotations.SerializedName

data class DescriptionModel (
    val text: String,
    @SerializedName("plain_text")
    val plainText: String
)