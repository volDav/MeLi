package com.drac.challenge.domain.useCase

import com.drac.challenge.domain.repository.DataRepository

class SearchQueryUseCase(private val dataRepository: DataRepository) {
    suspend fun execute(siteId: String, query: HashMap<String, String>) = dataRepository.getResults(siteId,query)
}