package com.example.marvelapp.framework.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.DbConstants.APP_DATABASE_NAME
import com.example.marvelapp.framework.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDataBase = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        APP_DATABASE_NAME
    ).build()

    @Provides
    fun provideFavoriteDao(appDataBase: AppDataBase) = appDataBase.favoriteDao()
}