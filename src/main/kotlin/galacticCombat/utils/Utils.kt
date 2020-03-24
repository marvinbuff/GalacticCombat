package galacticCombat.utils

import com.almasb.fxgl.entity.SpawnData
import javafx.geometry.Point2D

fun Double.toPoint(): Point2D = Point2D(this, this)

fun Pair<Int, Int>.toPoint(): Point2D = Point2D(first.toDouble(), second.toDouble())
fun Point2D.toIntegerPair() = x.toInt() to y.toInt()

val SpawnData.position: Point2D
  get() = Point2D(this.x, this.y)

operator fun Point2D.component1(): Double = x
operator fun Point2D.component2(): Double = y
operator fun Point2D.plus(other: Point2D): Point2D = Point2D(this.x + other.x, this.y + other.y)
operator fun Point2D.minus(other: Point2D): Point2D = Point2D(this.x - other.x, this.y - other.y)