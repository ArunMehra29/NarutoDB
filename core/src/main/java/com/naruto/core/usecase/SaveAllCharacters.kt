package com.naruto.core.usecase

import com.naruto.core.data.Character
import com.naruto.core.repository.CharacterRepository

class SaveAllCharacters(private val characterRepository: CharacterRepository)
{
    suspend operator fun invoke(characterList: List<Character>) =
        characterRepository.saveCharacters(characterList = characterList)
}