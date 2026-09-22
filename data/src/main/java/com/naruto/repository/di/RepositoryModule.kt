package com.naruto.repository.di

import com.naruto.core.repository.CharacterRepository
import com.naruto.repository.local.CharacterDao
import com.naruto.repository.remote.CharacterDbApi
import com.naruto.repository.repository.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        characterDbApi: CharacterDbApi,
        characterDao: CharacterDao
    ): CharacterRepository =
        CharacterRepositoryImpl(characterDbApi = characterDbApi, characterDao = characterDao)
}