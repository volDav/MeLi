package com.drac.challenge.presentation.ui.search

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SearchVM : ViewModel() {

    private val _badInput = MutableSharedFlow<Boolean>()
    val badInput: SharedFlow<Boolean> = _badInput

    private val _goToSearch = MutableSharedFlow<String>()
    val goToSearch: SharedFlow<String> = _goToSearch

    val input = ObservableField("")

    fun evaluateQuery() {
        if(input.get()?.isEmpty() == true) {
            viewModelScope.launch {
                _badInput.emit(true)
            }
        } else {
            viewModelScope.launch {
                _goToSearch.emit(input.get() ?: "")
            }
        }
    }

}