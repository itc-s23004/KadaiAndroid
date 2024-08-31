package jp.ac.it_college.std.s23004.kadai

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val name: String,
    val types: List<TypeInfo>
)

data class TypeInfo(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String,
    val url: String
)

data class TypeResponse(
    val damage_relations: DamageRelations
)

data class DamageRelations(
    val no_damage_to: List<Type>,
    val half_damage_to: List<Type>,
    val double_damage_to: List<Type>
)
data class PokemonSpeciesResponse(
    val names: List<Name>
)

data class Name(
    val name: String,
    val language: Language
)

data class Language(
    val name: String
)

