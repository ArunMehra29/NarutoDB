package com.narutodb.repository.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.naruto.core.data.Character
import com.naruto.core.data.Debut
import com.naruto.core.data.Family
import com.naruto.core.data.VoiceActors

data class DebutEntity (
    @SerializedName("novel") var novel: String? = null,
    @SerializedName("movie") var movie: String? = null,
    @SerializedName("appearsIn") var appearsIn: String? = null
)

data class FamilyEntity(
    @SerializedName("father") var father: String? = null,
    @SerializedName("mother") var mother: String? = null,
    @SerializedName("son") var son: String? = null,
    @SerializedName("daughter") var daughter: String? = null,
    @SerializedName("wife") var wife: String? = null,
    @SerializedName("adoptive son") var adoptiveSon: String? = null,
    @SerializedName("godfather") var godfather: String? = null
)

data class VoiceActorsEntity(
    @SerializedName("japanese") var japanese: Any? = null,
    @SerializedName("english") var english: Any? = null
)

data class GetAllCharacterResponse(

    @SerializedName("characters")
    var characters: List<CharacterEntity>? = null,

    @SerializedName("currentPage")
    var currentPage: String? = null,

    @SerializedName("pageSize")
    var pageSize: String? = null,

    @SerializedName("totalCharacters")
    var totalCharacters: Int? = null
)

@Entity(tableName = "character")
data class CharacterEntity (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("images")
    var images: List<String>? = null,

    @SerializedName("debut")
    var debutEntity: DebutEntity? = null,

    @SerializedName("family")
    var familyEntity: FamilyEntity? = null,

    @SerializedName("jutsu")
    var jutsu: List<String>? = null,

    @SerializedName("natureType")
    var natureType: List<String>? = null,

    @SerializedName("personal")
    var personal: Any? = null,

    @SerializedName("rank")
    var rank: Any? = null,

    @SerializedName("tools")
    var tools: List<String>? = null,

    @SerializedName("voiceActors")
    var voiceActorsEntity: VoiceActorsEntity? = null

)
{
    companion object
    {

        fun fromCharacterList(characterList: List<Character>): List<CharacterEntity>
        {
            val list = arrayListOf<CharacterEntity>()
            characterList.forEach { character: Character ->
                val characterEntity = fromCharacter(character = character)
                list.add(characterEntity)
            }
            return list
        }

        private fun fromCharacter(character: Character): CharacterEntity
        {
            val debut = getDebut(debut = character.debut)
            val family = getFamily(family = character.family)
            val voiceActors = getVoiceActors(voiceActors = character.voiceActors)
            return CharacterEntity(
                id = character.id,
                name = character.name,
                images = character.images,
                debutEntity = debut,
                voiceActorsEntity = voiceActors,
                personal = character.personal,
                rank = character.rank,
                tools = character.tools,
                jutsu = character.jutsu,
                natureType = character.natureType,
                familyEntity = family
            )
        }

        private fun getVoiceActors(voiceActors: VoiceActors?): VoiceActorsEntity
        {
            return VoiceActorsEntity(
                japanese = voiceActors?.japanese,
                english = voiceActors?.english
            )
        }

        private fun getFamily(family: Family?): FamilyEntity
        {
            return FamilyEntity(
                father = family?.father,
                mother = family?.mother,
                son = family?.son,
                daughter = family?.daughter,
                wife = family?.wife,
                adoptiveSon = family?.adoptiveSon,
                godfather = family?.godfather
            )
        }

        private fun getDebut(debut: Debut?): DebutEntity
        {
            return DebutEntity(
                novel = debut?.novel,
                movie = debut?.movie,
                appearsIn = debut?.appearsIn
            )
        }
    }

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