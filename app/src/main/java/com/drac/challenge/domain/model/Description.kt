package com.drac.challenge.domain.model

import com.google.gson.annotations.SerializedName

data class Description (
    val text: String,
    @SerializedName("plain_text")
    val plainText: String
)