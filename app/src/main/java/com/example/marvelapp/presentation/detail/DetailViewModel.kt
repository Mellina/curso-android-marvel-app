package com.example.marvelapp.presentation.detail

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.marvelapp.R
import com.example.marvelapp.extensions.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> get() = _uiState

    private val _favoriteUiState = MutableLiveData<FavoriteUiState>()
    val favoriteUiState: LiveData<FavoriteUiState> get() = _favoriteUiState

    init {
        _favoriteUiState.value = FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_unchecked)
    }

    fun getCharacterCategories(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(
            GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(
                characterId
            )
        ).watchStatus(loading = {
            _uiState.value = UIState.Loading
        }, success = { data ->

            val detailParentList = mutableListOf<DetailParentViewEntities>()

            val comics = data.first
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

            val events = data.second
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

            _uiState.value = if (detailParentList.isNotEmpty()) {
                UIState.Success(detailParentList)
            } else {
                UIState.Empty
            }
        }, error = {
            _uiState.value = UIState.Error
        })
    }

    fun updateFavorite(detailViewArg: DetailViewArg) = viewModelScope.launch {
        detailViewArg.run {
            addFavoriteUseCase.invoke(
                AddFavoriteUseCase.Params(characterId, name, imageUrl)
            ).watchStatus(
                loading = {
                    _favoriteUiState.value = FavoriteUiState.Loading
                },
                success = {
                    _favoriteUiState.value =
                        FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_checked)
                },
                error = {

                }
            )
        }
    }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val detailParentList: List<DetailParentViewEntities>) : UIState()
        object Error : UIState()
        object Empty : UIState()
    }

    sealed class FavoriteUiState {
        object Loading : FavoriteUiState()
        class FavoriteIcon(@DrawableRes val icon: Int) : FavoriteUiState()
    }

}


