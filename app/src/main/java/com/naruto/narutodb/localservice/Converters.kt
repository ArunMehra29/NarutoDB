package com.naruto.narutodb.localservice

import androidx.room.TypeConverter
import com.naruto.narutodb.model.common.response.DebutEntity
import com.naruto.narutodb.model.common.response.FamilyEntity
import com.naruto.narutodb.model.common.response.VoiceActorsEntity
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
    fun gsonToDebut(json: String?): DebutEntity?
    {
        return gson.fromJson(json, DebutEntity::class.java)
    }

    @TypeConverter
    fun debutToGson(debutEntity: DebutEntity?): String?
    {
        return gson.toJson(debutEntity)
    }

    @TypeConverter
    fun gsonToFamily(json: String?): FamilyEntity?
    {
        return gson.fromJson(json, FamilyEntity::class.java)
    }

    @TypeConverter
    fun familyToGson(familyEntity: FamilyEntity?): String?
    {
        return gson.toJson(familyEntity)
    }

    @TypeConverter
    fun gsonToVoiceActor(json: String?): VoiceActorsEntity?
    {
        return gson.fromJson(json, VoiceActorsEntity::class.java)
    }

    @TypeConverter
    fun voiceActorToGson(voiceActorsEntity: VoiceActorsEntity?): String?
    {
        return gson.toJson(voiceActorsEntity)
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>?>() {}.type
        return gson.fromJson(value, listType)
    }
}