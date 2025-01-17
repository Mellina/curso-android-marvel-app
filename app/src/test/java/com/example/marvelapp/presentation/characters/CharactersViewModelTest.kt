package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.bassul.testing.MainCoroutineRule
import com.bassul.testing.model.CharacterFactory
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharactersUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var getCharacterUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    private val charactersFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            charactersFactory.create(CharacterFactory.Hero.ThreeDMan),
            charactersFactory.create(CharacterFactory.Hero.ABomb)
        )
    )

    @Before
    fun setUp() {
        charactersViewModel = CharactersViewModel(
            getCharacterUseCase
        )
    }

    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() =
        runTest {
            whenever(
                getCharacterUseCase.invoke(
                    any()
                )
            ).thenReturn(
                flowOf(
                    pagingDataCharacters
                )
            )

            val result = charactersViewModel.charactersPagingData("")

            assertNotNull(result.first())

        }

    @Test(expected = RuntimeException::class)
    fun `should throw an exception when the calling to the use case returns an exception`() =
        runTest {
            whenever(
                getCharacterUseCase.invoke(
                    any()
                )
            ).thenThrow(
                RuntimeException()
            )

           charactersViewModel.charactersPagingData("")

        }

}
