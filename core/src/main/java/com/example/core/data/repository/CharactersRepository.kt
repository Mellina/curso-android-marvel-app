package com.example.core.data.repository

import androidx.paging.PagingSource
import com.example.core.domain.model.Character
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event

interface CharactersRepository {

    fun getCharacters(query: String): PagingSource<Int, Character>

    suspend fun getComic(characterId: Int): List<Comic>

    suspend fun getEvent(characterId: Int): List<Event>
}