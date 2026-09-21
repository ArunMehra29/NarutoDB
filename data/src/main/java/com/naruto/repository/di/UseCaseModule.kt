package com.naruto.repository.di

import com.naruto.core.repository.CharacterRepository
import com.naruto.core.usecase.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object UseCaseModule
{
    @Provides
    @Singleton
    fun provideGetCharactersUseCase(
        repository: CharacterRepository // Hilt fetches this from RepositoryModule
    ): GetCharactersUseCase {
        return GetCharactersUseCase(characterRepository = repository) // Manual instantiation
    }
}