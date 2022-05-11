package com.drac.challenge.domain.model

import com.google.gson.annotations.SerializedName

data class ObjectItems (
    @SerializedName("site_id")
    val siteI: String,

    val query: String,
    val paging: Paging,
    val results: List<Item>

)