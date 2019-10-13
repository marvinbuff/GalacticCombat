package galacticCombat.utils

import javafx.geometry.Point2D

fun Double.toPoint(): Point2D = Point2D(this, this)

fun Pair<Int, Int>.toPoint(): Point2D = Point2D(first.toDouble(), second.toDouble())