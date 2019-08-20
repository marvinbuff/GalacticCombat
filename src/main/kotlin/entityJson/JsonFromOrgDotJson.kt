package entityJson

import org.json.JSONArray
import org.json.JSONObject

open class JsonObjectWrapper() {
  protected val json = JSONObject()

  constructor(init: JsonObjectWrapper.() -> Unit) : this() {
    this.init()
  }

  infix fun String.to(value: JsonObjectWrapper) {
    json.put(this, value.json)
  }

  infix fun <T> String.to(value: T) {
    json.put(this, value)
  }

  override fun toString(): String {
    return json.toString(2)
  }
}

class SubJsonObjectWrapper() : JsonObjectWrapper() {

  constructor(init: SubJsonObjectWrapper.() -> Unit) : this() {
    this.init()
  }

  infix fun toName(value: String) {
    json.put("name", value)
  }

}

fun main() {


  val entity = SubJsonObjectWrapper {
    "name" to "marvin"
    this toName "Marvin"
  }

  val json = JsonObjectWrapper {
    "name" to "Roy"
    "body" to entity
    "cars" to JSONArray().apply {
      put("Tesla")
      put("Porsche")
      put("BMW")
      put("Ferrari")
    }
  }

  println(json)

}