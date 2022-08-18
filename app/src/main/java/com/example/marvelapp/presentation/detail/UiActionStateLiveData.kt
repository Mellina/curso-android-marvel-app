package com.example.marvelapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.marvelapp.R
import com.example.marvelapp.extensions.watchStatus
import kotlin.coroutines.CoroutineContext

class UiActionStateLiveData(
    private val coroutineContext: CoroutineContext,
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) {

    private val action = MutableLiveData<Action>()
    val state : LiveData<UIState> = action.switchMap {
        liveData(coroutineContext) {
            when(it) {
                is Action.Load -> {
                    getCharacterCategoriesUseCase(
                        GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(
                            it.characterId
                        )).watchStatus(loading = {
                        emit(UIState.Loading)
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

                        if (detailParentList.isNotEmpty()) {
                            emit(UIState.Success(detailParentList))
                        } else {
                            emit(UIState.Empty)
                        }
                    }, error = {
                        emit(UIState.Error)

                    })
                }
            }
        }
    }

    fun load(characterId: Int){
        action.value = Action.Load(characterId)
    }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val detailParentList: List<DetailParentViewEntities>) : UIState()
        object Error : UIState()
        object Empty : UIState()
    }

    sealed class Action {
        data class Load(val characterId: Int): Action()
    }
}