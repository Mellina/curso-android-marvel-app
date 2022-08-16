package com.example.marvelapp.framework

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource
): FavoritesRepository {
    override suspend fun getAll(): Flow<List<Character>> {
        return favoritesLocalDataSource.getAll()
    }

    override suspend fun saveFavorite(character: Character) {
        return favoritesLocalDataSource.save(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        return favoritesLocalDataSource.delete(character)
    }
}