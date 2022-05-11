package com.drac.challenge.data.impl

import com.drac.challenge.common.ResultOrError
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.model.ObjectItems
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.network.MeliApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepositoryImpl (private val meliApi: MeliApi) : DataRepository {

    override suspend fun getResults(siteId: String, query: HashMap<String, String>): ResultOrError<ObjectItems, Throwable> = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getItems(siteId, query))
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

    override suspend fun getDetailItem(itemId: String): ResultOrError<Item, Throwable> = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getFullItem(itemId))
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

}