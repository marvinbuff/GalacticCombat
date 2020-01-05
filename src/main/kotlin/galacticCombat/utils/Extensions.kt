package galacticCombat.utils

import com.almasb.fxgl.entity.SpawnData
import javafx.geometry.Point2D

fun Double.toPoint(): Point2D = Point2D(this, this)

fun Pair<Int, Int>.toPoint(): Point2D = Point2D(first.toDouble(), second.toDouble())

val SpawnData.position: Point2D
  get() = Point2D(this.x, this.y)

operator fun Point2D.component1(): Double = x
operator fun Point2D.component2(): Double = y