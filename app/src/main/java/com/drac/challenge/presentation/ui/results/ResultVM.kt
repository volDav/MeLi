package com.drac.challenge.presentation.ui.results

import androidx.lifecycle.*
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.common.Variables.category
import com.drac.challenge.common.Variables.limit
import com.drac.challenge.common.Variables.limitValue
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.common.Variables.offset
import com.drac.challenge.common.Variables.q
import com.drac.challenge.di.MainDispatcher
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.presentation.common.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultVM @Inject constructor(
    private val searchQueryUseCase: SearchQueryUseCase,
    @MainDispatcher
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val results: ArrayList<Item> = arrayListOf()

    private val _stateRequest = MutableStateFlow<State<Boolean>>(State.Empty())
    val stateRequest : StateFlow<State<Boolean>> = _stateRequest

    private val _loadRecycler = MutableLiveData<List<Item>>(listOf())
    val loadRecycler: LiveData<List<Item>> = _loadRecycler

    private val map by lazy {
        hashMapOf(
            q to "",
            limit to limitValue,
            offset to "${results.size}"
        )
    }

    fun evaluatePaging(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int) {
        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
            isNotRequesting {
                executeQuery()
            }
        }
    }

    fun setQueryData(p0: String) {
        map[q] = p0
        isNotRequesting {
            executeQuery()
        }
    }

    fun setCategoryData(p0: String) {
        map[category] = p0
        isNotRequesting {
            executeQuery()
        }
    }

    fun executeQuery() {

        _stateRequest.value = State.Loading()
        viewModelScope.launch(dispatcher) {
            //This delay is to give user better experience with  the popup "Loading"
            delay(500)

            map[offset] = "${results.size}"

            val rta = searchQueryUseCase.getResults(mco,map)

            when(rta) {
                is ResultOrError.Result -> {
                    _stateRequest.value = State.Success(true)
                    updateAndSetResultToRecycler(rta.p0)
                }
                is ResultOrError.Fail -> {
                    _stateRequest.value = State.Error(rta.p0.message ?: "")
                }
            }
        }
    }

    private fun isNotRequesting(callback: () -> Unit) {
        if(_stateRequest.value !is State.Loading){
            callback.invoke()
        }
    }

    private fun updateAndSetResultToRecycler(p0: List<Item>) {
        results.addAll(p0)
        if (results.isNotEmpty()) {
            _loadRecycler.value = results
        }
    }

}