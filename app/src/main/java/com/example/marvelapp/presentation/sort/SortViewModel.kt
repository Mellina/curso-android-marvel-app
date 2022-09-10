package com.example.marvelapp.presentation.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.GetCharactersSortingUseCase
import com.example.core.usecase.SaveCharacterSortingUseCase
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.marvelapp.extensions.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor(
    private val getCharactersSortingUseCase: GetCharactersSortingUseCase,
    private val saveCharacterSortingUseCase: SaveCharacterSortingUseCase,
    private val coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UIState> = action.switchMap { action ->
        liveData(coroutinesDispatchers.main()) {
            when (action) {
                Action.GetStoredSorting -> {
                    getCharactersSortingUseCase.invoke()
                        .collect { sortingPair ->
                            emit(UIState.SortingResult(sortingPair))
                        }
                }

                is Action.ApplySorting -> {
                    val orderBy = action.orderBy
                    val order = action.order

                    saveCharacterSortingUseCase.invoke(
                        SaveCharacterSortingUseCase.Params(orderBy to order)
                    ).watchStatus(
                        loading = {
                            emit(UIState.ApplyState.Loading)
                        },
                        success = {
                            emit(UIState.ApplyState.Success)
                        },
                        error = {
                            emit(UIState.ApplyState.Error)
                        }
                    )

                }
            }
        }

    }

    init {
        action.value = Action.GetStoredSorting
    }

    fun applySorting(
        orderBy: String,
        order: String
    ) {
        action.value = Action.ApplySorting(orderBy, order)
    }

    sealed class UIState {
        data class SortingResult(val storedSorting: Pair<String, String>) : UIState()

        sealed class ApplyState : UIState() {
            object Loading : ApplyState()
            object Success : ApplyState()
            object Error : ApplyState()
        }
    }

    sealed class Action {
        object GetStoredSorting : Action()
        data class ApplySorting(
            val orderBy: String,
            val order: String
        ) : Action()
    }
}