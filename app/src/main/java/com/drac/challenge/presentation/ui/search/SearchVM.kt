package com.drac.challenge.presentation.ui.search

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.common.Variables.mco
import com.drac.challenge.di.MainDispatcher
import com.drac.challenge.domain.model.Category
import com.drac.challenge.domain.useCase.SearchQueryUseCase
import com.drac.challenge.presentation.common.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val state: SavedStateHandle,
    private val useCase: SearchQueryUseCase,
    @MainDispatcher
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _stateRequest = MutableStateFlow<State<Boolean>>(State.Empty())
    val stateRequest : StateFlow<State<Boolean>> = _stateRequest

    private val _badInput = MutableSharedFlow<Boolean>()
    val badInput: SharedFlow<Boolean> = _badInput

    private val _goToSearch = MutableSharedFlow<String>()
    val goToSearch: SharedFlow<String> = _goToSearch

    private val _loadRecycler = MutableLiveData<List<Category>>(listOf())
    val loadRecycler: LiveData<List<Category>> = _loadRecycler

    val input = ObservableField("")

    init {
        val saved = state.get("query") ?: ""
        input.set(saved)
    }

    fun getCategories() {
        if(loadRecycler.value?.isEmpty() == true ) {
            requestCategories()
        }
    }

    fun evaluateQuery() {
        viewModelScope.launch(dispatcher) {
            if(input.get()?.isEmpty() == true) {
                _badInput.emit(true)
            } else {
                _goToSearch.emit(input.get() ?: "")
            }
        }
    }

    private fun requestCategories() {
        _stateRequest.value = State.Loading()
        viewModelScope.launch(dispatcher) {
            //This delay is to give user better experience with  the popup "Loading"
            delay(500)

            val rta = useCase.getCategories(mco)

            when(rta) {
                is ResultOrError.Result -> {
                    _stateRequest.value = State.Success(true)
                    notifyResult(rta.p0)
                }
                is ResultOrError.Fail -> {
                    _stateRequest.value = State.Error(rta.p0.message ?: "")
                }
            }
        }
    }

    private fun notifyResult(p0: List<Category>) {
        if (p0.isNotEmpty()) {
            _loadRecycler.value = p0
        }
    }

    fun addCallbacks() {
        input.addOnPropertyChangedCallback(obQuery)
    }

    private fun removeCallbacks() {
        input.removeOnPropertyChangedCallback(obQuery)
    }

    private val obQuery = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val toSave = input.get() ?: ""
            state.set("query", toSave)
        }
    }


    override fun onCleared() {
        removeCallbacks()
        super.onCleared()
    }
}