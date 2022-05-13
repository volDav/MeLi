package com.drac.challenge.domain.useCase

import com.drac.challenge.domain.repository.DataRepository
import javax.inject.Inject


class DetailItemUseCase @Inject constructor (
     private val dataRepository: DataRepository
) {

    suspend fun getDetailItem(itemId: String) = dataRepository.getDetailItem(itemId)

    suspend fun getDescription(itemId: String) = dataRepository.getDescription(itemId)
}