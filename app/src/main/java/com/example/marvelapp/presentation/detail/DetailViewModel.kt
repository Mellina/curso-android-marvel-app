package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> get() = _uiState

    fun getCharacterCategories(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(
            GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(
                characterId
            )
        ).watchStatus()
    }

    private fun kotlinx.coroutines.flow.Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
        viewModelScope.launch {
            collect { status ->
                _uiState.value = when (status) {
                    ResultStatus.Loading -> UIState.Loading
                    is ResultStatus.Success -> {

                        val detailParentList = mutableListOf<DetailParentViewEntities>()

                        val comics = status.data.first
                        if (comics.isNotEmpty()) {
                            comics.map {
                                DetailChildViewEntities(
                                    it.id,
                                    it.imageUrl
                                )
                            }.also {
                                detailParentList.add(
                                    DetailParentViewEntities(
                                        R.string.details_comics_category,
                                        it
                                    )
                                )
                            }
                        }

                        val events = status.data.second
                        if (events.isNotEmpty()) {
                            events.map {
                                DetailChildViewEntities(it.id, it.imageUrl)
                            }.also {
                                detailParentList.add(
                                    DetailParentViewEntities(
                                        R.string.details_events_category,
                                        it
                                    )
                                )
                            }
                        }

                        if (detailParentList.isNotEmpty()) {
                            UIState.Success(detailParentList)
                        } else {
                            UIState.Empty
                        }

                    }
                    is ResultStatus.Error -> UIState.Error
                }

            }
        }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val detailParentList: List<DetailParentViewEntities>) : UIState()
        object Error : UIState()
        object Empty : UIState()
    }
}


