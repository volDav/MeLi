package com.drac.challenge.presentation.ui.search

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.drac.challenge.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val state: SavedStateHandle,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _badInput = MutableSharedFlow<Boolean>()
    val badInput: SharedFlow<Boolean> = _badInput

    private val _goToSearch = MutableSharedFlow<String>()
    val goToSearch: SharedFlow<String> = _goToSearch

    val input = ObservableField("")

    init {
        val saved = state.get("query") ?: ""
        input.set(saved)
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

    fun addCallbacks() {
        input.addOnPropertyChangedCallback(obQuery)
    }

    private fun removeCallbacks() {
        input.removeOnPropertyChangedCallback(obQuery)
    }

    private val obQuery = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val toSave = input.get() ?: ""
            state.set("query",toSave)
        }
    }

    override fun onCleared() {
        removeCallbacks()
        super.onCleared()
    }
}