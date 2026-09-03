package com.naruto.narutodb.ui.character

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.LinkedTreeMap
import com.naruto.core.data.Character
import com.naruto.core.data.InfoSection
import com.naruto.narutodb.datasourcemanager.CharacterDataSourceManager
import com.naruto.narutodb.util.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.naruto.core.data.Result
import kotlin.collections.get

class CharacterViewModel: ViewModel()
{

    var characters: MutableState<Result<List<Character>?>> = mutableStateOf(value = Result.Loading)

    var characterDetail : MutableState<Result<Character>> = mutableStateOf(value = Result.Loading)

    private var characterList: List<Character>? = null

    fun setSelectedCharacter(character: Character)
    {
        character.infoSections = getCharacterInfo(character = character)
        characterDetail.value = Result.Success(data = character)
    }

    private fun displayError(message: String?)
    {
        val exception = Exception(message)
        characters.value = Result.Error(exception = exception)
    }

    private var getCharacterByIdExceptionHandler:
            CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.debug("fatal", "getCharacterByIdExceptionHandler exception value == ${throwable.message}")
        displayCharacterDetailError(throwable.message)
    }

    fun getAllCharacters()
    {
        viewModelScope.launch(context = Dispatchers.IO)
        {
            withContext(Dispatchers.Main)
            {
                characters.value = Result.Loading
            }
            val response = CharacterDataSourceManager.getInstance().getAllCharacters()
            response?.let { value ->
                characterList = value
                withContext(Dispatchers.Main)
                {
                    characters.value = Result.Success(value)
                }
                //saving fetched value to local DB
                CharacterDataSourceManager.getInstance().saveCharacterListToLocal(response)
            } ?: run {
                withContext(Dispatchers.Main)
                {
                    displayError("Unable to fetch character list")
                }
            }
        }
    }

    fun filterCharacterById(characterId: Int?) {
        val character = characterList?.firstOrNull { character ->
            character.id == characterId
        }
        character?.also { value ->
            characters.value = Result.Success(arrayListOf(value))
        } ?: run {
            characters.value = Result.Success(arrayListOf())
        }
    }

    fun repostValue()
    {
        characterList?.also { value ->
            characters.value = Result.Success(value)
        } ?: run {
            characters.value = Result.Success(arrayListOf())
        }
    }

    private fun displayCharacterDetailError(message: String?)
    {
        val exception = Exception(message)
        characterDetail.value = Result.Error(exception = exception)
    }

    private fun getCharacterById(id: Int?)
    {
        viewModelScope.launch(Dispatchers.IO + getCharacterByIdExceptionHandler)
        {
            val response = CharacterDataSourceManager.getInstance().getCharacterById(id = id)
            Logger.debug("fatal", "response value == $response")
            withContext(Dispatchers.Main)
            {
                response?.also { value ->
                    value.infoSections = getCharacterInfo(character = value)
                    characterDetail.value = Result.Success(value)
                } ?: run {
                    val exception = Exception("Unable to fetch character details")
                    characterDetail.value = Result.Error(exception = exception)
                }
            }
        }
    }

    private fun getCharacterInfo(character: Character): List<InfoSection>
    {
        val infoList = ArrayList<InfoSection>()
        val birthday = (character.personal as? LinkedTreeMap<*, *>)?.get("birthdate")
        birthday?.let { dob ->
            infoList.add(InfoSection(title = "Date of birth", content = dob as String))
        }

        val gender = (character.personal as? LinkedTreeMap<*, *>)?.get("sex")
        gender?.let {
            infoList.add(InfoSection(title = "Gender", content = it as String))
        }

        val natureType = character.natureType
        var natureTypeString = ""
        natureType?.let { type ->
            type.forEach { item ->
                natureTypeString += "$item \n"
            }
        }

        if (natureTypeString.isNotEmpty())
        {
            infoList.add(InfoSection(title = "Nature type", content = natureTypeString))
        }

        val team = (character.personal as? LinkedTreeMap<*, *>)?.get("team")
        var teamString = ""
        team?.let { teamList ->
            if (teamList is ArrayList<*>)
            {
                teamList.forEach { item ->
                    teamString += "$item \n"
                }
            }
        }

        if (teamString.isNotEmpty())
        {
            infoList.add(InfoSection(title = "Team", content = teamString))
        }

        val clan = (character.personal as? LinkedTreeMap<*, *>)?.get("clan")
        clan?.let {
            infoList.add(InfoSection(title = "Clan", content = it as String))
        }

        return infoList
    }
}