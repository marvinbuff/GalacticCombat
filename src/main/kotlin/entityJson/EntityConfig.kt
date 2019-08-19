package entityJson

// entityJson.Json has an array of entities.
// Entities have a name, active flag, state, lc, .., array of clusters.
// A cluster has id, label, managed flag and an array of segments.
// A segment

fun main() {

  val json = json {
    "msg" map "hello world"
    "user" map json {
      "anotherDepth" map "hallo"
    }
  }

  println(json.render())

}

class Entity : Json()

//TODO create Entity and stuff. and check how it works with arrays.