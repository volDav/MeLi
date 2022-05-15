package com.drac.challenge.presentation.ui.detail

import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drac.challenge.common.ResultOrError
import com.drac.challenge.domain.model.Item
import com.drac.challenge.domain.useCase.DetailItemUseCase
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.common.loadImageFromUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailVM @Inject constructor(
    private val detailItemUseCase: DetailItemUseCase
) : ViewModel() {

    private var idItem: String = ""

    private val _stateRequest = MutableStateFlow<State<Unit>>(State.Empty())
    val stateRequest : StateFlow<State<Unit>> = _stateRequest

    private val _fullItem = MutableLiveData<Item>()
    val fullItem : LiveData<Item> = _fullItem

    fun setId(idItem: String) {
        this.idItem = idItem
    }

    fun executeRequest() {

        _stateRequest.value = State.Loading()
        viewModelScope.launch {

            val job1 = async  { detailItemUseCase.getDetailItem(idItem) }
            val job2 = async  { detailItemUseCase.getDescription(idItem) }

            val detail = job1.await()
            val desc = job2.await()


            if(detail is ResultOrError.Result) {

                val auxItem = detail.p0

                if(desc is ResultOrError.Result){
                    auxItem.description = desc.p0
                }

                _stateRequest.value = State.Success(Unit)
                _fullItem.value = auxItem
            } else {
                _stateRequest.value = State.Error("")
            }
        }
    }

    fun loadImageFromUrl(imageView: AppCompatImageView, url : String) {
        imageView.loadImageFromUrl(url)
    }

}