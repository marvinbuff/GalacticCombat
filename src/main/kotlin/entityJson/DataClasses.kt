package entityJson

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify

@UnstableDefault private val configuration = JsonConfiguration(
  encodeDefaults = true,
  strictMode = true,
  unquoted = false,
  prettyPrint = true,
  indent = "  ",
  useArrayPolymorphism = false,
  classDiscriminator = "type"
)

@Serializable
data class Entities(val entities: List<Entity>)

@Serializable
data class Entity(val name: String, val active: Boolean = true, val state: String = "integrated", val lc: String, val clusters: List<Cluster>)

@Serializable
data class Cluster(val id: String, val label: String, val managed: Boolean = true, val segments: List<Segment>)

@Serializable
data class Segment(val type: String, val id: String, val strategy: String = "ask", val label: String, val params: List<String> = listOf())

private fun setupEntities(): Entities {
  val defaultParams = listOf("4142", "4632", "4638", "8786", "8849")
  val defaultSegments = listOf(
    Segment("P_IGT_COMP", "P_IGT_COMP", "ask", "IG Prod. & Trad.", defaultParams),
    Segment("REST", "REST", label = "Remaining")
  )
  val defaultClusters = listOf(
    Cluster("CK", "Care Solutions", true, defaultSegments)
  )

  val entityA = Entity("4142", true, lc = "USD", clusters = defaultClusters)

  return Entities(listOf(entityA))
}

@ImplicitReflectionSerializer
@UnstableDefault fun main() {
  val json = Json(configuration)

  // Entity to String
  val dataEntities = setupEntities()
  val dataJson = json.stringify(dataEntities)

  // String to Entity
  val readJson = json.parseJson(input)
  val readEntities = json.fromJson<Entities>(readJson)

  println("Print the stringified Data Class")
  println(dataJson)
  println("-----------------------------------------------------------------")
  println("Print the parsed Json")
  println(input)
  println("-----------------------------------------------------------------")
  println("Compare objects")
  println("Entities objects equality: ${dataEntities == readEntities}")
  println("String objects equality: ${dataJson == json.stringify(readEntities)}")
}


private const val input = """{
  "entities": [
    {
      "name": "4142",
      "active": true,
      "state": "integrated",
      "lc": "USD",
      "clusters": [
        {
          "id": "CK",
          "label": "Care Solutions",
          "managed": true,
          "segments": [
            {
              "type": "P_IGT_COMP",
              "id": "P_IGT_COMP",
              "strategy": "ask",
              "label": "IG Prod. & Trad.",
              "params": [
                "4142",
                "4632",
                "4638",
                "8786",
                "8849"
              ]
            },
            {
              "type": "REST",
              "id": "REST",
              "strategy": "ask",
              "label": "Remaining",
              "params": [
              ]
            }
          ]
        }
      ]
    }
  ]
}"""