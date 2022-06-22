package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.base.AppCoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharacterCategoriesUseCase {
    operator fun invoke(params: GetCharacterCategoriesParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCharacterCategoriesParams(val characterId: Int)
}

class GetCharacterCategoriesUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val dispatchers: AppCoroutinesDispatchers
) : UseCase<GetCharacterCategoriesUseCase.GetCharacterCategoriesParams,
        Pair<List<Comic>, List<Event>>>(),
    GetCharacterCategoriesUseCase {

    override suspend fun doWork(
        params: GetCharacterCategoriesUseCase.GetCharacterCategoriesParams
    ): ResultStatus<Pair<List<Comic>, List<Event>>> {

        return withContext(dispatchers.io) {
            val comicsDeferred = async {
                charactersRepository.getComic(params.characterId)
            }
            val eventsDeferred = async {
                charactersRepository.getEvent(params.characterId)
            }
            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }

    }

}