package com.drac.challenge.domain.model

data class Item (
    val id: String,
    val title: String,
    val price: Long,
    val availableQuantity: Int,
    val soldQuantity: Int,
    val thumbnail: String,

    var pictures: List<Picture>? = null,
    var description: Description? = null,

    var favorite: Boolean = false
)