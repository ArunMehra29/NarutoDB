package com.naruto.core.repository

import com.naruto.core.data.Character

class CharacterRepository(private val characterRepository: ICharacterRepository)
{

    suspend fun getAllCharactersFromRemote() = characterRepository.getAllCharactersFromRemote()

    suspend fun getAllCharactersFromLocal() = characterRepository.getAllCharactersFromLocal()

    suspend fun getCharacterById(characterId: Int) =
        characterRepository.getCharacterById(characterId = characterId)

    suspend fun saveCharacters(characterList: List<Character>) =
        characterRepository.saveCharacters(characterList = characterList)

}