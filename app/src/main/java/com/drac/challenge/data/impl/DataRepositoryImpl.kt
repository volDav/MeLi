package com.drac.challenge.data.impl

import com.drac.challenge.common.ResultOrError
import com.drac.challenge.data.network.MeliApi
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.repository.DataRepository
import com.drac.challenge.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor (
    private val meliApi: MeliApi
) : DataRepository {

    override suspend fun getResults(siteId: String, query: HashMap<String, String>) = withContext(Dispatchers.IO) {
        try {
            val result = meliApi.getItems(siteId, query).results
            if(result.isNotEmpty()) {
                ResultOrError.Result(result.map { it.toDomain() })
            } else {
                ResultOrError.Fail(NullPointerException("Empty List"))
            }

        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

    override suspend fun getDetailItem(itemId: String) = withContext(Dispatchers.IO) {
        try {
            val response = meliApi.getFullItem(itemId)
            if(response.pictures?.isNotEmpty() == true) {
                ResultOrError.Result(response.toDomain())
            } else {
                ResultOrError.Fail(NullPointerException("Empty picture list"))
            }
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

    override suspend fun getDescription(itemId: String) = withContext(Dispatchers.IO) {
        try {
            ResultOrError.Result(meliApi.getDescription(itemId).toDomain())
        } catch (e: Exception) {
            ResultOrError.Fail(e)
        }
    }

}