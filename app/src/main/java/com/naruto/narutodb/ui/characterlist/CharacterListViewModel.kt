package com.naruto.narutodb.ui.characterlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.core.data.Character
import com.naruto.narutodb.datasourcemanager.CharacterDataSourceManager
import com.naruto.narutodb.model.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterListViewModel: ViewModel()
{

    var characters: MutableState<Result<List<Character>?>> = mutableStateOf(Result.Loading)

    private var characterList: List<Character>? = null

    init
    {
        getAllCharacters()
    }

    private fun displayError(message: String?)
    {
        val exception = Exception(message)
        characters.value = Result.Error(exception = exception)
    }

    private fun getAllCharacters()
    {
        viewModelScope.launch(Dispatchers.IO)
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
}