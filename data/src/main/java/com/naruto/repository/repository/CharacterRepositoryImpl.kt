package com.naruto.repository.repository

import com.naruto.core.data.Character
import com.naruto.core.repository.CharacterRepository
import com.naruto.repository.di.CharacterDbApi

class CharacterRepositoryImpl(private val characterDbApi: CharacterDbApi) : CharacterRepository
{

    override suspend fun getAllCharacters(): List<Character>?
    {
        val body = characterDbApi.getALlCharacters()
        val characterEntityList = body.characters
        return characterEntityList?.map { characterEntity -> characterEntity.toCharacter() }
    }

    override suspend fun saveCharactersToDB(characterList: List<Character>?) {
        //currently we do not do anything here
    }
}