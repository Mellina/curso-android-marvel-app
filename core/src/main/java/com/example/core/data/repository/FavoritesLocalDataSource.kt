package com.example.core.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.core.domain.model.Character

interface FavoritesLocalDataSource {

    fun getAll(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun save(character: Character)

    suspend fun delete(character: Character)
}