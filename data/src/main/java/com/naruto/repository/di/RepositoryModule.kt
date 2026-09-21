package com.naruto.repository.di

import com.naruto.core.repository.CharacterRepository
import com.naruto.repository.repository.CharacterRepositoryImpl
import dagger.Binds
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
    fun provideCharacterRepository(characterDbApi: CharacterDbApi): CharacterRepository =
        CharacterRepositoryImpl(characterDbApi = characterDbApi)
}