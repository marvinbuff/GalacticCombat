package entityJson

fun main() {

  val json = json {
    "msg" map "hello world"
    "user" map json {
      "anotherDepth" map "hallo"
    }
  }

  println(json.render())

}

private const val BASE_INDENT = "  "

open class Json internal constructor() {
  private val entries = arrayListOf<Pair<String, Any>>()

  infix fun String.map(value: Any) {
    entries.add(this to value)
  }

  fun render(): String {
    val builder = StringBuilder()
    render(builder)
    return builder.toString()
  }

  fun render(builder: StringBuilder, depth: Int = 0) {
    val indent = StringBuilder().run {
      repeat(depth) { append(BASE_INDENT) }
      toString()
    }
    builder.append("{").append("\n")
    for ((k, v) in entries) {
      with(builder) {
        append(indent).append(""""$k" : """)
        when (v) {
          is String   -> append(""""$v"""")
          is Json     -> v.render(builder, depth + 1)
//          is Array<Json> -> append("[${v.joinToString()}]")
          is Array<*> -> append("[${v.joinToString()}]")
          else        -> append("$v")
        }
        append("\n")
      }
    }
    builder.append(indent).append("}")
  }

}

fun json(init: Json.() -> Unit): Json {
  val obj = Json()
  obj.init()
  return obj
}





