package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.usecase.GetComicsUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
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
            collect { status ->
                _uiState.value = when (status) {
                    ResultStatus.Loading -> UIState.Loading
                    is ResultStatus.Success -> {
                        val detailChildList = status.data.map {
                            DetailChildViewEntities(id = it.id, it.imageUrl)
                        }
                        val detailParentList = listOf(
                            DetailParentViewEntities(
                                R.string.details_comics_category,
                                detailChildList
                            )
                        )
                        UIState.Success(detailParentList)
                    }
                    is ResultStatus.Error -> UIState.Error
                }

            }
        }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val detailParentList: List<DetailParentViewEntities>) : UIState()
        object Error : UIState()

    }
}


