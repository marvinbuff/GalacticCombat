package entityJson

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
data class Entities(val entities: List<Entity>)

@Serializable
data class Entity(val name: String)

@UnstableDefault val configuration = JsonConfiguration(
  encodeDefaults = true,
  strictMode = true,
  unquoted = false,
  prettyPrint = true,
  indent = "  ",
  useArrayPolymorphism = false,
  classDiscriminator = "type"
)

@UnstableDefault fun main() {
  val json = Json(configuration)
  val entityA = Entity("Entity A")
  val entityB = Entity("Entity B")
  val entities = Entities(listOf(entityA, entityB))

  val entitiesJson = json.stringify(Entities.serializer(), entities)
  println(entitiesJson)
}