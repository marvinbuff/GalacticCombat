package javaFxGame.game.pysics

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

typealias Vector = Pair<Double, Double>

fun main() {
  println("Test GetDirectionVectorFromAngle")
  val rad = listOf(0.0, PI / 2, PI, (PI / 2) * 3, 2 * PI)
  val res = rad.map { it to getDirectionVectorFromAngle(it) }
  res.forEach { println("${it.first} -> ${it.second}") }

  println("\nTest GetAngleBetweenVectors")
  val pairs = listOf(
    (1.0 to 0.0) to (1.0 to 0.0),
    (1.0 to 0.0) to (1.0 to 1.0),
    (1.0 to 0.0) to (0.0 to 1.0),
    (1.0 to 1.0) to (1.0 to 0.0),
    (1.0 to 1.0) to (1.0 to 1.0),
    (1.0 to 1.0) to (0.0 to 1.0)
  )
  pairs.map { it to getAngleBetweenVectors(it.first, it.second) }
    .forEach { println("${it.first} -> ${it.second}") }
}

fun getDirectionVectorFromAngle(rad: Double): Vector {
  val epsilon = 0.00001
  var x = cos(rad)
  var y = sin(rad)
  if (abs(x) < epsilon) x = 0.0
  if (abs(y) < epsilon) y = 0.0
  return x to y
}

fun getAngleBetweenVectors(first: Vector, second: Vector): Double = atan2(second.y() - first.y(), second.x() - first.x())


//region------------------- Vector --------------------------
fun Vector.x() = first

fun Vector.y() = second

private operator fun Vector.div(double: Double) = x() / double to y() / double
//endregion