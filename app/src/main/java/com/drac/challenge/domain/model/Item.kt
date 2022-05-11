package com.drac.challenge.domain.model

import com.google.gson.annotations.SerializedName

data class Item (
    val id: String,
    val title: String,
    val price: Long,
    @SerializedName("available_quantity")
    val availableQuantity: Int,
    @SerializedName("sold_quantity")
    val soldQuantity: Int,
    val thumbnail: String
)