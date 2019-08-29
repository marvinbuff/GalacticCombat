package jsonFun

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault


@UnstableDefault
@ImplicitReflectionSerializer fun main() {
  val book = readSpellbook()
  printJson(book)

  val cards = SpellCards.create(book)
  printJson(cards)
}