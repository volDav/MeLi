package com.drac.challenge.presentation.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.common.Variables.limit
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.common.Variables.offset
import com.drac.challenge.common.Variables.q
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.presentation.common.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultVM @Inject constructor(
    private val searchQueryUseCase: SearchQueryUseCase
) : ViewModel() {

    private val results: ArrayList<Item> = arrayListOf()

    private val _stateRequest = MutableStateFlow<State<Boolean>>(State.Empty())
    val stateRequest : StateFlow<State<Boolean>> = _stateRequest

    private val _loadRecycler = MutableStateFlow<List<Item>>(listOf())
    val loadRecycler: StateFlow<List<Item>> = _loadRecycler

    private val map by lazy {
        hashMapOf(
            q to "",
            limit to "15",
            offset to "${results.size}"
        )
    }

    fun setQueryData(query: String) {
        map[q] = query
    }

    fun executeQuery() {
        setResultToRecycler()

        _stateRequest.value = State.Loading()
        viewModelScope.launch {
            delay(500)

            map[offset] = "${results.size}"

            val rta = searchQueryUseCase.getResults(mco,map)

            when(rta) {
                is ResultOrError.Result -> {
                    results.addAll(rta.p0)
                    _stateRequest.value = State.Success(true)
                    setResultToRecycler()
                }
                is ResultOrError.Fail -> {
                    _stateRequest.value = State.Error(rta.p0.message ?: "")
                }
            }
        }

    }

    private fun setResultToRecycler() {
        _loadRecycler.value = results
    }

}