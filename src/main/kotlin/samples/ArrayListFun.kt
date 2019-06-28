package samples

fun main() {
  val classes = listOf("A", "B", "C")
  val rows = listOf(Pair("A", "hallo"), Pair("B", "nice"), Pair("A", "hunger"), Pair("C", "awesome"), Pair("B", "ok"))
  val rowsByClass = Array<ArrayList<String>>(classes.size, ::ArrayList)

  classes.forEach { rowsByClass[classes.indexOf(it)] = ArrayList<String>() }

  rows.forEach { rowsByClass[classes.indexOf(it.first)].add(it.second) }

  rowsByClass.forEach { println(it) }
}