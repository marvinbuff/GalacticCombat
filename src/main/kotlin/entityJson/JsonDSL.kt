package entityJson

/**
 * Here I implemented a fundamental Json DSL using only base Kotlin.
 */
fun main() {

  val json = jsonDSL {
    "msg" map "hello world"
    "user" map jsonDSL {
      "anotherDepth" map "hallo"
    }
    "array" map jsonArrayDSL {
      add("x")
      add("y")
      add(jsonDSL {
        "deepest" map "level 3"
      })
    }
  }

  println(json.render())

}

private const val BASE_INDENT = "  "

open class JsonDSL internal constructor() {
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
    val indent = getIndent(depth)
    builder.append("{").append("\n")
    for ((k, v) in entries) {
      with(builder) {
        append(indent).append(""""$k" : """)
        when (v) {
          is String       -> append(""""$v"""")
          is JsonDSL      -> v.render(builder, depth + 1)
          is JsonArrayDSL -> v.render(builder, depth + 1)
          is Array<*>     -> append("[${v.joinToString()}]")
          else            -> append("$v")
        }
        append("\n")
      }
    }
    builder.append(indent).append("}")
  }

}

open class JsonArrayDSL internal constructor() {
  private val entries = arrayListOf<Any>()

  infix fun add(value: Any) {
    entries.add(value)
  }

  fun render(builder: StringBuilder, depth: Int = 0) {
    val indent = getIndent(depth)
    builder.append("[").append("\n")
    for (k in entries) {
      with(builder) {
        append(indent)
        when (k) {
          is String   -> append(""""$k"""")
          is JsonDSL  -> k.render(builder, depth + 1)
          is Array<*> -> append("[${k.joinToString()}]")
          else        -> append("$k")
        }
        append(", \n")
      }
    }
    builder.append(indent).append("}")
  }
}

// ----------------- Global Functions -----------------------

fun jsonDSL(init: JsonDSL.() -> Unit): JsonDSL {
  val obj = JsonDSL()
  obj.init()
  return obj
}

fun jsonArrayDSL(init: JsonArrayDSL.() -> Unit): JsonArrayDSL {
  val obj = JsonArrayDSL()
  obj.init()
  return obj
}

// ------------------ Private Utils -------------------------

fun getIndent(depth: Int): String =
  StringBuilder().run {
    repeat(depth) { append(BASE_INDENT) }
    toString()
  }





