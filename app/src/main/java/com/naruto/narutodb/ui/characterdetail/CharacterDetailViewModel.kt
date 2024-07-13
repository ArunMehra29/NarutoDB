package com.naruto.narutodb.ui.characterdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.LinkedTreeMap
import com.naruto.core.data.Character
import com.naruto.core.data.InfoSection
import com.naruto.narutodb.model.common.Result
import com.naruto.narutodb.datasourcemanager.CharacterDataSourceManager
import com.naruto.narutodb.util.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
)
    : ViewModel()
{

    var characterDetail : MutableState<Result<Character>> = mutableStateOf(Result.Loading)

    private var getCharacterByIdExceptionHandler:
            CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.debug("fatal", "getCharacterByIdExceptionHandler exception value == ${throwable.message}")
        displayError(throwable.message)
    }

    init
    {
        val characterId = savedStateHandle.get<Int>("character_key")
        Logger.debug("fatal", "character Id == $characterId")
        getCharacterById(id = characterId)
    }

    private fun displayError(message: String?)
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