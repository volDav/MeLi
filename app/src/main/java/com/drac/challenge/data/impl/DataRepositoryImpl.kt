package com.drac.challenge.data.impl

import com.drac.challenge.common.ResultOrError
import com.drac.challenge.domain.model.Description
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.model.ObjectItems
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.network.MeliApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor (
    private val meliApi: MeliApi
) : DataRepository {

    override suspend fun getResults(siteId: String, query: HashMap<String, String>): ResultOrError<ObjectItems, Exception> = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getItems(siteId, query))
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

    override suspend fun getDetailItem(itemId: String): ResultOrError<Item, Exception> = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getFullItem(itemId))
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

    override suspend fun getDescription(itemId: String): ResultOrError<Description, Exception> = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getDescription(itemId))
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

}