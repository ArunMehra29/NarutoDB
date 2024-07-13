package com.naruto.narutodb.datasource

import com.naruto.core.data.Character
import com.naruto.core.repository.ICharacterRepository
import com.naruto.narutodb.localservice.LocalService
import com.naruto.narutodb.model.common.response.CharacterEntity
import com.naruto.narutodb.remoteservice.RemoteService

class CharacterDataSource(
    private val remoteService: RemoteService = RemoteService.getInstance(),
    private val localService: LocalService? = LocalService.getInstance()
): ICharacterRepository
{

    override suspend fun getAllCharactersFromRemote(): List<Character>?
    {
        val body = remoteService.getAllCharacters()
        val characterEntityList = body.characters
        return characterEntityList?.map { characterEntity -> characterEntity.toCharacter() }
    }

    override suspend fun getAllCharactersFromLocal(): List<Character>?
    {
        val characterEntityList = localService?.characterDao()?.getAllCharacters()
        return characterEntityList?.map { characterEntity -> characterEntity.toCharacter() }
    }

    override suspend fun saveCharacters(characterList: List<Character>?)
    {
        if (characterList != null)
        {
            val charactersEntityList = CharacterEntity.fromCharacterList(characterList = characterList)
            localService?.characterDao()?.saveCharacters(characters = charactersEntityList)
        }
    }

    override suspend fun getCharacterById(characterId: Int): Character?
    {
        val character = localService?.characterDao()?.getCharacterById(id = characterId)
        return character?.toCharacter()
    }
}