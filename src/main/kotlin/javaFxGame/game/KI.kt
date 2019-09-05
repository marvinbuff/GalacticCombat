package javaFxGame.game

import javaFxGame.game.World.Companion.world
import kotlin.math.atan2

class KI(private val ship: PlayerShip) {

  fun run() {
    val other = world.ships.first()
    val theta = 180.0 / Math.PI * atan2(other.x - ship.x, ship.y - other.y)
    val action = when {
      theta < 1.0  -> Action.TURN_LEFT
      theta > -1.0 -> Action.TURN_RIGHT
      else         -> Action.SHOOT
    }

    ship.executeActions(setOf(action))
  }

}