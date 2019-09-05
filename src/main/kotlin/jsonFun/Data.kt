@file:Suppress("ArrayInDataClass", "SpellCheckingInspection")

package jsonFun

import kotlinx.serialization.Serializable

@Serializable
data class Spellbook(
  val name: String,
  val owner: String,
  val spells: Array<Spell>
)


// Spell: name, cast time, classes, description, duration, level, material, range, ritual, somatic, verbal, vsm string.
@Serializable
data class Spell(
  val name: String,
  val school: String,
  val level: String,
  val ritual: Boolean = false,
  val castTime: String = "Instantaneous",
  val duration: String = "Instantaneous",
  val concentration: Boolean = false,
  val material: Boolean = false,
  val materialText: String = "",
  val somatic: Boolean = false,
  val verbal: Boolean = false,
  val description: String,
  val source: String
)

