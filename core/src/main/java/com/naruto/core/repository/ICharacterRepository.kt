package com.naruto.core.repository

import com.naruto.core.data.Character

interface ICharacterRepository
{

    suspend fun getAllCharactersFromRemote(): List<Character>?

    suspend fun getAllCharactersFromLocal(): List<Character>?

    suspend fun getCharacterById(characterId: Int): Character?

    suspend fun saveCharacters(characterList: List<Character>?)

}