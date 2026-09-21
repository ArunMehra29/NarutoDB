package com.naruto.core.usecase

import com.naruto.core.repository.CharacterRepository

class GetCharactersUseCase(private val characterRepository: CharacterRepository)
{
    suspend operator fun invoke() = characterRepository.getAllCharacters()
}