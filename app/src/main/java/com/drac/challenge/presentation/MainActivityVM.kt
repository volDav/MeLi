package com.drac.challenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.data.impl.DataRepositoryImpl
import com.drac.challenge.di.NetworkModule
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {


    init {

        val retrofit = NetworkModule.provideRetrofit()
        val api = NetworkModule.provideApiInterface(retrofit)
        val impl = DataRepositoryImpl(api)
        val cc = SearchQueryUseCase(impl)

        viewModelScope.launch {
            val query = hashMapOf(
                "q" to "Headphone Inalambrico",
                "offset" to "0",
                "limit" to "20"
            )
            val result = cc.execute("MCO", query)

            when (result) {
                is ResultOrError.Fail -> {}
                is ResultOrError.Result -> {}
            }

        }

    }

    fun hello() {

    }

}