package com.naruto.core.data

data class Character(
    var id: Int? = null,
    var name: String? = null,
    var images: List<String>? = null,
    var debut: Debut? = null,
    var family: Family? = null,
    var jutsu: List<String>? = null,
    var natureType: List<String>? = null,
    var personal: Any? = null,
    var rank: Any? = null,
    var tools: List<String>? = null,
    var voiceActors: VoiceActors? = null,
    var infoSections: List<InfoSection>? = null
)

data class Debut (
    var novel: String? = null,
    var movie: String? = null,
    var appearsIn: String? = null
)

data class Family(
    var father: String? = null,
    var mother: String? = null,
    var son: String? = null,
    var daughter: String? = null,
    var wife: String? = null,
    var adoptiveSon: String? = null,
    var godfather: String? = null
)

data class VoiceActors(
    var japanese: Any? = null,
    var english: Any? = null
)