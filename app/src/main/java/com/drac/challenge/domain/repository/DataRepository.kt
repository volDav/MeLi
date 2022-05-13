package com.drac.challenge.domain.repository

import com.drac.challenge.common.ResultOrError
import com.drac.challenge.domain.model.Description
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.model.ObjectItems

interface DataRepository {

    suspend fun getResults(siteId: String, query: HashMap<String, String>) : ResultOrError<ObjectItems, Exception>
    suspend fun getDetailItem(itemId: String) : ResultOrError<Item, Exception>
    suspend fun getDescription(itemId: String) : ResultOrError<Description, Exception>
}