package com.naruto.narutodb.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.naruto.narutodb.model.common.response.CharacterEntity

@Dao
interface CharacterDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<CharacterEntity>?

    @Query("SELECT * FROM character WHERE id= :id")
    suspend fun getCharacterById(id: Int?): CharacterEntity?
}