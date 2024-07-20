package com.narutodb.repository.localservice

import androidx.room.TypeConverter
import com.narutodb.repository.response.DebutEntity
import com.narutodb.repository.response.FamilyEntity
import com.narutodb.repository.response.VoiceActorsEntity
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken

class Converters
{

    private val gson: Gson = Gson()

    @TypeConverter
    fun gsonToAny(json: String?): Any?
    {
        return gson.fromJson(json, Any::class.java)
    }

    @TypeConverter
    fun anyToGson(any: Any?): String?
    {
        return gson.toJson(any)
    }

    @TypeConverter
    fun gsonToDebut(json: String?): com.narutodb.repository.response.DebutEntity?
    {
        return gson.fromJson(json, com.narutodb.repository.response.DebutEntity::class.java)
    }

    @TypeConverter
    fun debutToGson(debutEntity: com.narutodb.repository.response.DebutEntity?): String?
    {
        return gson.toJson(debutEntity)
    }

    @TypeConverter
    fun gsonToFamily(json: String?): com.narutodb.repository.response.FamilyEntity?
    {
        return gson.fromJson(json, com.narutodb.repository.response.FamilyEntity::class.java)
    }

    @TypeConverter
    fun familyToGson(familyEntity: com.narutodb.repository.response.FamilyEntity?): String?
    {
        return gson.toJson(familyEntity)
    }

    @TypeConverter
    fun gsonToVoiceActor(json: String?): com.narutodb.repository.response.VoiceActorsEntity?
    {
        return gson.fromJson(json, com.narutodb.repository.response.VoiceActorsEntity::class.java)
    }

    @TypeConverter
    fun voiceActorToGson(voiceActorsEntity: com.narutodb.repository.response.VoiceActorsEntity?): String?
    {
        return gson.toJson(voiceActorsEntity)
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>?>() {}.type
        return gson.fromJson(value, listType)
    }
}