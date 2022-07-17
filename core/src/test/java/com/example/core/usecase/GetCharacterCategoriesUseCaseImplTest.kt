package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.usecase.base.CoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.model.ComicFactory
import com.example.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException
import kotlin.Exception

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var charactersRepository: CharactersRepository

    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))
    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Before
    fun setUp() {
        getCharacterCategoriesUseCase = GetCharacterCategoriesUseCaseImpl(
            charactersRepository,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            //Arrange
            whenever(charactersRepository.getComic(character.id)).thenReturn(comics)
            whenever(charactersRepository.getEvent(character.id)).thenReturn(events)

            //Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            //Assert
            assertEquals(result.toList()[0], ResultStatus.Loading)
            assertTrue(result.toList()[1] is ResultStatus.Success)
        }

    //@Test(expected = RuntimeException::class)
    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
           /* //Arrange
            whenever(charactersRepository.getComic(character.id)).thenReturn(comics)
            whenever(charactersRepository.getEvent(character.id)).thenThrow(Exception())*/

            whenever(charactersRepository.getEvent(character.id)).thenAnswer { throw Throwable() }


            //Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            //Assert
            assertEquals(result.toList()[0], ResultStatus.Loading)
            assertTrue(result.toList()[1] is ResultStatus.Error)
        }

    @Test(expected = RuntimeException::class)
    fun `should return Error from ResultStatus when get comics request returns error`() =
        runTest {
            //Arrange
            whenever(charactersRepository.getComic(character.id)).thenThrow(Exception())
            whenever(charactersRepository.getEvent(character.id)).thenReturn(events)

            //Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            //Assert
            assertEquals(result.toList()[0], ResultStatus.Loading)
            assertTrue(result.toList()[1] is ResultStatus.Error)
        }
}