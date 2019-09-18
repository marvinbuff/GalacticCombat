package samples

fun main() {
  val postitiveTests = listOf("hallo:23", "super:hallo")
  val negativeTests = listOf("a", "hallo", "23,23.23", "222.322", "", "3333")
  println("Positive Tests")
  postitiveTests.forEach(::onlyMatchWithCurrency)
}

//fun matchMapEntry(line: String) {
//  val finding = """(\d{1,3}(\.\d{3})*)(,\d+)*""".toRegex().find(line)?.value
//  if (finding != null) {
//    val format = NumberFormat.getInstance(Locale.GERMANY) // to have 200.230,20 be parsed with comma as separator.
//    val doubleValue = format.parse(finding).toDouble()
//    print(" as Double: $doubleValue")
//  }
//  println()
//}

