package com.naruto.repository.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.naruto.core.data.Character
import com.naruto.core.data.Debut
import com.naruto.core.data.Family
import com.naruto.core.data.VoiceActors

data class DebutEntity (
    @SerializedName(value = "novel") var novel: String? = null,
    @SerializedName(value = "movie") var movie: String? = null,
    @SerializedName(value = "appearsIn") var appearsIn: String? = null
)

data class FamilyEntity(
    @SerializedName(value = "father") var father: String? = null,
    @SerializedName(value = "mother") var mother: String? = null,
    @SerializedName(value = "son") var son: String? = null,
    @SerializedName(value = "daughter") var daughter: String? = null,
    @SerializedName(value = "wife") var wife: String? = null,
    @SerializedName(value = "adoptive son") var adoptiveSon: String? = null,
    @SerializedName(value = "godfather") var godfather: String? = null
)

data class VoiceActorsEntity(
    @SerializedName(value = "japanese") var japanese: Any? = null,
    @SerializedName(value = "english") var english: Any? = null
)

data class GetAllCharacterResponse(

    @SerializedName(value = "characters")
    var characters: List<CharacterEntity>? = null,

    @SerializedName(value = "currentPage")
    var currentPage: String? = null,

    @SerializedName(value = "pageSize")
    var pageSize: String? = null,

    @SerializedName(value = "totalCharacters")
    var totalCharacters: Int? = null
)

@Entity(tableName = "character")
data class CharacterEntity (
    @PrimaryKey(autoGenerate = false)
    @SerializedName(value = "id")
    var id: Int? = null,

    @SerializedName(value = "name")
    var name: String? = null,

    @SerializedName(value = "images")
    var images: List<String>? = null,

    @SerializedName(value = "debut")
    var debutEntity: DebutEntity? = null,

    @SerializedName(value = "family")
    var familyEntity: FamilyEntity? = null,

    @SerializedName(value = "jutsu")
    var jutsu: List<String>? = null,

    @SerializedName(value = "natureType")
    var natureType: List<String>? = null,

    @SerializedName(value = "personal")
    var personal: Any? = null,

    @SerializedName(value = "rank")
    var rank: Any? = null,

    @SerializedName(value = "tools")
    var tools: List<String>? = null,

    @SerializedName(value = "voiceActors")
    var voiceActorsEntity: VoiceActorsEntity? = null

)
{

    fun toCharacter(): Character
    {
        val debut = getDebut(debutEntity = debutEntity)
        val family = getFamily(familyEntity = familyEntity)
        val voiceActors = getVoiceActors(voiceActorsEntity = voiceActorsEntity)
        return Character(
            id = id,
            name = name,
            images = images,
            debut = debut,
            voiceActors = voiceActors,
            personal = personal,
            rank = rank,
            tools = tools,
            jutsu = jutsu,
            natureType = natureType,
            family = family
        )
    }

    private fun getVoiceActors(voiceActorsEntity: VoiceActorsEntity?): VoiceActors
    {
        return VoiceActors(
            japanese = voiceActorsEntity?.japanese,
            english = voiceActorsEntity?.english
        )
    }

    private fun getFamily(familyEntity: FamilyEntity?): Family
    {
        return Family(
            father = familyEntity?.father,
            mother = familyEntity?.mother,
            son = familyEntity?.son,
            daughter = familyEntity?.daughter,
            wife = familyEntity?.wife,
            adoptiveSon = familyEntity?.adoptiveSon,
            godfather = familyEntity?.godfather
        )
    }

    private fun getDebut(debutEntity: DebutEntity?): Debut
    {
        return Debut(
            novel = debutEntity?.novel,
            movie = debutEntity?.movie,
            appearsIn = debutEntity?.appearsIn
        )
    }
}