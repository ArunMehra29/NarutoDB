package com.naruto.core.usecase

import com.naruto.core.repository.CharacterRepository

class GetCharacterById(private val characterRepository: CharacterRepository)
{
    suspend operator fun invoke(id: Int) = characterRepository.getCharacterById(characterId = id)
}