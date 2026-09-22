package com.naruto.repository.repository

import com.naruto.core.data.Character
import com.naruto.core.repository.CharacterRepository
import com.naruto.repository.local.CharacterDao
import com.naruto.repository.remote.CharacterDbApi

class CharacterRepositoryImpl(
    private val characterDbApi: CharacterDbApi,
    private val characterDao: CharacterDao
) : CharacterRepository
{
    override suspend fun getAllCharacters(): List<Character>?
    {
        var list = characterDao.getAllCharacters()
        if (list.isNullOrEmpty())
        {
            list = characterDbApi.getALlCharacters().characters
            characterDao.saveCharacters(characters = list ?: emptyList())
        }
        return list?.map { data -> data.toCharacter() }
    }
}