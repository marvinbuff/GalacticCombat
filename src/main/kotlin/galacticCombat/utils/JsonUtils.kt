package galacticCombat.utils

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration.Companion.Stable
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import java.io.PrintWriter
import java.net.URL


inline fun <reified T : Any> loadJson(url: URL): T = parseJson(url.readText())

@OptIn(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> parseJson(text: String): T = jsonSerializer.parse(text)

@OptIn(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> writeJson(url: URL, dataObject: T) {
  val data = jsonSerializer.stringify(dataObject)
  val writer = PrintWriter(url.file)
  writer.use { w ->
    w.write(data)
    w.flush()
  }
}

@OptIn(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> printJson(dataObject: T) {
  val data = jsonSerializer.stringify(dataObject)
  println("Json: $data")
}

val jsonSerializer = Json(Stable.copy(prettyPrint = true, indent = "  "))