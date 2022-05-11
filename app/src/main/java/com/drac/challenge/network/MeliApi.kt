package com.drac.challenge.network

import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.model.ObjectItems
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MeliApi {

    @GET("sites/{siteId}/search")
    suspend fun getItems(
        @Path("siteId") siteId: String,
        @QueryMap query: HashMap<String, String>
    ): ObjectItems

    @GET("items/{itemId}")
    suspend fun getFullItem(
        @Path("itemId") itemId: String
    ): Item

/*
    @GET("items/{itemId}/description")
    suspend fun getItemDescription(
        @Path("itemId") itemId: String
    ): ItemDescription
*/
}