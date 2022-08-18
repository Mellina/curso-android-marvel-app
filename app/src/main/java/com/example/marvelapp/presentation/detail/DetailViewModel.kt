package com.example.marvelapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.base.CheckFavoriteUseCase
import com.example.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutineDispatchers: CoroutinesDispatchers
) : ViewModel() {

    val categories = UiActionStateLiveData(
        coroutineDispatchers.main(),
        getCharacterCategoriesUseCase
    )

    val favorite = FavoriteUiActionStateLiveData(
        coroutineDispatchers.main(),
        checkFavoriteUseCase,
        addFavoriteUseCase
    )

}


