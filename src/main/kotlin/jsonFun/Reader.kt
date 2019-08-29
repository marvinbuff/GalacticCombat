@file:Suppress("EXPERIMENTAL_API_USAGE")

package jsonFun

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import kotlinx.serialization.stringify


@ImplicitReflectionSerializer
fun readSpellbook(): Spellbook {
  val input = loadResource("/data/spellBookTemplate.json")

  return json.parse(input)
}

private fun loadResource(resource: String): String =
  try {
    object {}.javaClass.getResource(resource)
      .readText(Charsets.UTF_8) // File("src/main/resources/data/..").readText()
  } catch (all: Exception) {
    throw RuntimeException("Failed to load resource=$resource!", all)
  }

@ImplicitReflectionSerializer
inline fun <reified T : Any> printJson(jsonObject: T) {
  println(json.stringify(jsonObject))
}

private val configuration = JsonConfiguration(
  encodeDefaults = false,
  strictMode = true,
  unquoted = false,
  prettyPrint = true,
  indent = "  ",
  useArrayPolymorphism = false,
  classDiscriminator = "type"
)

val json = Json(configuration)