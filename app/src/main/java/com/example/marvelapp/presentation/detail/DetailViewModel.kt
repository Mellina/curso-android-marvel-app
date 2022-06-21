package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.usecase.GetComicsUseCase
import com.example.core.usecase.base.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getComicsUseCase: GetComicsUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> get() = _uiState

    fun getComics(characterId: Int) = viewModelScope.launch {
        getComicsUseCase(GetComicsUseCase.GetComicsParams(characterId)).watchStatus()
    }

    private fun kotlinx.coroutines.flow.Flow<ResultStatus<List<Comic>>>.watchStatus() =
        viewModelScope.launch {
            collect { result ->
                _uiState.value = when (result) {
                    ResultStatus.Loading -> UIState.Loading
                    is ResultStatus.Success -> UIState.Success(result.data)
                    is ResultStatus.Error -> UIState.Error
                }

            }
        }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val comics: List<Comic>) : UIState()
        object Error : UIState()

    }
}


