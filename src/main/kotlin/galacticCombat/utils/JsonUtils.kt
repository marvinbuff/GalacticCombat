package galacticCombat.utils

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import java.io.PrintWriter
import java.net.URL

@UseExperimental(ImplicitReflectionSerializer::class, UnstableDefault::class)
inline fun <reified T : Any> loadJson(url: URL): T = jsonSerializer.parse(url.readText())

@UseExperimental(ImplicitReflectionSerializer::class, UnstableDefault::class)
inline fun <reified T : Any> writeJson(url: URL, dataObject: T) {
  val data = jsonSerializer.stringify(dataObject)
  val writer = PrintWriter(url.file)
  writer.use { w ->
    w.write(data)
    w.flush()
  }
}

@UnstableDefault
private val jsonConfiguration = JsonConfiguration(
    encodeDefaults = true,
    strictMode = true,
    unquoted = false,
    prettyPrint = true,
    indent = "  ",
    useArrayPolymorphism = false,
    classDiscriminator = "type"
)

@UnstableDefault
val jsonSerializer = Json(jsonConfiguration)