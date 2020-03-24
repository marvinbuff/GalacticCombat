package galacticCombat.utils

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration.Companion.Stable
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import java.io.PrintWriter
import java.net.URL

@UseExperimental(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> loadJson(url: URL): T = jsonSerializer.parse(url.readText())

@UseExperimental(ImplicitReflectionSerializer::class)
inline fun <reified T : Any> writeJson(url: URL, dataObject: T) {
  val data = jsonSerializer.stringify(dataObject)
  val writer = PrintWriter(url.file)
  writer.use { w ->
    w.write(data)
    w.flush()
  }
}

private val jsonConfiguration = Stable.copy(prettyPrint = true)

@UnstableDefault
val jsonSerializer = Json(jsonConfiguration)