package com.drac.challenge.domain.useCase

import com.drac.challenge.domain.repository.DataRepository
import javax.inject.Inject

class SearchQueryUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend fun getResults(siteId: String, query: HashMap<String, String>) = dataRepository.getResults(siteId,query)

}