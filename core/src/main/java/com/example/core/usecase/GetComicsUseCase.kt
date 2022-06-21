package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.domain.model.Comic
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetComicsUseCase {
    operator fun invoke(params: GetComicsParams): Flow<ResultStatus<List<Comic>>>

    data class GetComicsParams(val characterId: Int)
}

class GetComicsUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
) : UseCase<GetComicsUseCase.GetComicsParams, List<Comic>>(),
    GetComicsUseCase {

    override suspend fun doWork(params: GetComicsUseCase.GetComicsParams): ResultStatus<List<Comic>> {
        val comics = charactersRepository.getComic(params.characterId)
        return ResultStatus.Success(comics)
    }

}