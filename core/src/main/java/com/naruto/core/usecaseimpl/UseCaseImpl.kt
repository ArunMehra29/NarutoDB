package com.naruto.core.usecaseimpl

import com.naruto.core.usecase.GetAllCharactersFromLocal
import com.naruto.core.usecase.GetAllCharactersFromRemote
import com.naruto.core.usecase.GetCharacterById
import com.naruto.core.usecase.SaveAllCharacters

data class UseCaseImpl(
    var getAllCharactersFromLocal: GetAllCharactersFromLocal,
    var getAllCharactersFromRemote: GetAllCharactersFromRemote,
    var getCharacterById: GetCharacterById,
    var saveAllCharacters: SaveAllCharacters
)