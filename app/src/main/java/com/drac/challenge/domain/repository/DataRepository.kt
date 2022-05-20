package com.drac.challenge.domain.repository

import com.drac.challenge.common.ResultOrError
import com.drac.challenge.domain.model.Category
import com.drac.challenge.domain.model.Description
import com.drac.challenge.domain.model.Item

interface DataRepository {

    suspend fun getCategories(siteId: String) : ResultOrError<List<Category>, Exception>
    suspend fun getResults(siteId: String, query: HashMap<String, String>) : ResultOrError<List<Item>, Exception>
    suspend fun getDetailItem(itemId: String) : ResultOrError<Item, Exception>
    suspend fun getDescription(itemId: String) : ResultOrError<Description, Exception>
}