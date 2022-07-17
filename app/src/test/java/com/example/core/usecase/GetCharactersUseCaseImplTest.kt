package com.example.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.bassul.testing.MainCoroutineRule
import com.bassul.testing.model.CharacterFactory
import com.bassul.testing.pagingsource.PagingSourceFactory
import com.example.core.data.repository.CharactersRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: CharactersRepository

    lateinit var getCharacterUseCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingSource = PagingSourceFactory().create(listOf(hero))

    @Before
    fun setUp() {
        getCharacterUseCase = GetCharactersUseCaseImpl(
            repository
        )
    }

    @Test
    fun `should validate flow paging data creation when invoke from use case is called`() =
        runTest {
            whenever(repository.getCharacters(""))
                .thenReturn(fakePagingSource)
            val result = getCharacterUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    "",
                    PagingConfig(20)
                )
            )

            verify(repository).getCharacters("")

            assertNotNull(result.first())
        }
}