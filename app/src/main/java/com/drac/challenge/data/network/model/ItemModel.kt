package com.drac.challenge.data.network.model

import com.google.gson.annotations.SerializedName

data class ItemModel(
    val id: String,
    val title: String,
    val price: Long,
    @SerializedName("available_quantity")
    val availableQuantity: Int,
    @SerializedName("sold_quantity")
    val soldQuantity: Int,
    val thumbnail: String,

    var pictures: List<PictureModel>? = null,
    var description: DescriptionModel? = null
)