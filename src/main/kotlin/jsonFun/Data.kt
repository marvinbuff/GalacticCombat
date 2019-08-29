@file:Suppress("ArrayInDataClass", "SpellCheckingInspection")

package jsonFun

import kotlinx.serialization.Serializable

@Serializable
data class Spellbook(
  val name: String,
  val owner: String,
  val spells: Array<Spell>
)

@Serializable
data class Spell(
  val name: String,
  val school: String,
  val level: String,
  val concentration: Boolean = false,
  val description: String,
  val source: String
)

