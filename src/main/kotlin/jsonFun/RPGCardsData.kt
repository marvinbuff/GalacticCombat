@file:Suppress("ArrayInDataClass", "SpellCheckingInspection")

package jsonFun

import kotlinx.serialization.Serializable

@Serializable
data class SpellCards(
  val spells: Array<SpellCard>
) {
  companion object {
    fun create(book: Spellbook): SpellCards {
      return SpellCards(book.spells.map { SpellCard.create(it) }.toTypedArray())
    }
  }
}

@Serializable
data class SpellCard(
  val name: String,
  val school: String,
  val level: String,
  val description: String
) {
  companion object {
    fun create(spell: Spell): SpellCard {
      return SpellCard(spell.name, spell.school, spell.level, spell.description)
    }
  }
}
// count, colour, title, icon, icon_back, contents[strings], tags
// contents: subtitle, property casting time, property range, property components, rule, text