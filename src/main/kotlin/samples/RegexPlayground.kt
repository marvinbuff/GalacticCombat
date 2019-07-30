package samples

fun main() {
  val postitiveTests = listOf("203.202,23 EUR", "202,32 EUR", "0,23 CAN", "1.000,100", "23,22", "203.222.233.233,23", "$ 20,23", "-$ 233,23", "ABC -230.222,23")
  val negativeTests = listOf("23", "a", "hallo", "23,23.23", "222.322", "")
  println("Positive Tests")
  postitiveTests.forEach(::onlyMatchWithMoneyValue)
  println("Negative Tests")
  negativeTests.forEach(::onlyMatchWithMoneyValue)
  println("Match Currency")
  postitiveTests.forEach(::onlyMatchWithCurrency)
}

fun onlyMatchWithMoneyValue(line: String) {
  val doesMatch = if ("""^.*(\d{1,3}(\.\d{3})*)(,\d+)+.*""".toRegex().matches(line)) "+" else "-"
  val negated = """.*-.*""".toRegex().matches(line)
  val finding = """(\d{1,3}(\.\d{3})*)(,\d+)+""".toRegex().find(line)?.value
  println("${doesMatch}Line: '$line' finds ${if (negated) "-" else ""}$finding")
}

fun onlyMatchWithCurrency(line: String) {
  val elements = line.split(" ")
  elements.forEach {
    when {
      it.matches(""".*\$""".toRegex()) -> {
        println("$line -> Dollar"); return
      }
      it.matches("""\w*""".toRegex())  -> {
        println("$line -> $it"); return
      }
    }
  }
  println("$line -> Unknown Currency")
}