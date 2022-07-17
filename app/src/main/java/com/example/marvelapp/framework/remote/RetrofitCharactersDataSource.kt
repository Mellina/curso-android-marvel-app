package com.example.marvelapp.framework.remote

import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.core.domain.model.CharacterPaging
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.example.marvelapp.framework.network.response.toComicModel
import com.example.marvelapp.framework.network.response.toEventModel
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharactersRemoteDataSource {

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging {
        val data = marvelApi.getCharacters(queries).data
        val character = data.results.map {
            it.toCharacterModel()
        }

        return CharacterPaging(
            data.offset,
            data.total,
            character
        )
    }

    override suspend fun fetchComics(characterId: Int): List<Comic> {
        return marvelApi.getComics(characterId).data.results.map {
            it.toComicModel()
        }
    }

    override suspend fun fetchEvents(characterId: Int): List<Event> {
        return marvelApi.getEvents(characterId).data.results.map {
            it.toEventModel()
        }
    }
}