package com.naruto.narutodb.datasourcemanager

import com.naruto.core.data.Character
import com.naruto.core.repository.CharacterRepository
import com.naruto.core.usecase.GetAllCharactersFromLocal
import com.naruto.core.usecase.GetAllCharactersFromRemote
import com.naruto.core.usecase.GetCharacterById
import com.naruto.core.usecase.SaveAllCharacters
import com.naruto.core.usecaseimpl.UseCaseImpl
import com.narutodb.repository.datasource.CharacterDataSource
import com.naruto.narutodb.util.Logger


//create an interface to expose methods that can be called
class CharacterDataSourceManager
{

    companion object
    {
        @Volatile
        private var instance: CharacterDataSourceManager? = null

        fun getInstance() = instance?: synchronized(this)
        {
            instance?: CharacterDataSourceManager()
        }
    }

    private val characterRepository = CharacterRepository(characterRepository = CharacterDataSource())

    private val useCaseImpl = UseCaseImpl(
        getAllCharactersFromLocal = GetAllCharactersFromLocal(characterRepository = characterRepository),
        getAllCharactersFromRemote = GetAllCharactersFromRemote(characterRepository = characterRepository),
        getCharacterById = GetCharacterById(characterRepository = characterRepository),
        saveAllCharacters = SaveAllCharacters(characterRepository = characterRepository)
    )


    suspend fun getAllCharacters(): List<Character>?
    {
        try
        {
            val response = getCharactersFromRemote()
            return response
        }
        catch (exception : Exception)
        {
            Logger.debug(
                "fatal",
                "allCharactersLocalExceptionHandler exception value == ${exception.message}"
            )
            return getCharactersFromLocal()
        }
    }

    suspend fun saveCharacterListToLocal(characters: List<Character>?)
    {
        useCaseImpl.saveAllCharacters.invoke(characterList = characters ?: arrayListOf())
    }

    suspend fun getCharacterById(id: Int?): Character?
    {
        return useCaseImpl.getCharacterById.invoke(id = id ?: 0)
    }

    private suspend fun getCharactersFromRemote(): List<Character>?
    {
        return useCaseImpl.getAllCharactersFromRemote.invoke()
    }

    private suspend fun getCharactersFromLocal(): List<Character>?
    {
        return useCaseImpl.getAllCharactersFromLocal.invoke()
    }
}