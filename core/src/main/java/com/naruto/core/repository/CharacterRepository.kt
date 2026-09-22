package com.naruto.core.repository

import com.naruto.core.data.Character

interface CharacterRepository
{
    suspend fun getAllCharacters(): List<Character>?
}