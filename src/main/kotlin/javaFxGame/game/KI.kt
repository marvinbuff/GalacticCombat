package javaFxGame.game

import javaFxGame.game.World.Companion.world
import javaFxGame.game.pysics.getDirectionVectorFromAngle
import javaFxGame.game.pysics.x
import javaFxGame.game.pysics.y
import kotlin.math.atan2

class KI(private val ship: PlayerShip) {

  fun run() {
    val other = world.ships.first()
    val toOther = other.x - ship.x to other.y - ship.y
    val dir = getDirectionVectorFromAngle(ship.rotation)
    val theta = atan2(toOther.y() - dir.y(), toOther.x() - dir.x()) // atan2 dy/dx
    val action = when {
      theta < 0.01  -> Action.TURN_LEFT
      theta > -0.01 -> Action.TURN_RIGHT
      else          -> Action.SHOOT
    }

    ship.executeActions(setOf(action))
  }

}