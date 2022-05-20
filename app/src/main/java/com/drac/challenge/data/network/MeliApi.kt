package com.drac.challenge.data.network

import com.drac.challenge.data.network.model.CategoryModel
import com.drac.challenge.data.network.model.DescriptionModel
import com.drac.challenge.data.network.model.ItemModel
import com.drac.challenge.data.network.model.ObjectItemsModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MeliApi {

    @GET("sites/{siteId}/categories")
    suspend fun getCategories(
        @Path("siteId") siteId: String
    ): List<CategoryModel>

    @GET("sites/{siteId}/search")
    suspend fun getItems(
        @Path("siteId") siteId: String,
        @QueryMap query: HashMap<String, String>
    ): ObjectItemsModel

    @GET("items/{itemId}")
    suspend fun getFullItem(
        @Path("itemId") itemId: String
    ): ItemModel

    @GET("items/{itemId}/description")
    suspend fun getDescription(
        @Path("itemId") itemId: String
    ): DescriptionModel

}