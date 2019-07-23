package javaFxGame.game

import javaFxGame.game.World.Companion.world
import javafx.scene.image.Image
import kotlin.math.max

class PlayerShip(image: AnimatedImage, x: Double, y: Double) : Actor(image, x, y), IControllable {
  override val worldOffset: Double = 0.0
  var shootingCooldown: Double = 0.0

  override fun act(time: Double) {
    super.act(time)
    world.meteors.forEach { meteor ->
      if (this.intersectWith(meteor)) {
        world.markedDeath.add(this)
      }
    }
    shootingCooldown -= time
    shootingCooldown = max(shootingCooldown, 0.0)
  }

  override fun executeActions(actions: Set<Action>) {
    val rotationSpeed = 3.0
    val accelerationAmount = 0.2
    actions.forEach { action ->
      when (action) {
        Action.TURN_LEFT  -> rotate(-rotationSpeed)
        Action.TURN_RIGHT -> rotate(rotationSpeed)
        Action.ACCELERATE -> accelerate(accelerationAmount)
        Action.DECELERATE -> accelerate(-accelerationAmount)
        Action.SHOOT      -> shoot()
        Action.OTHER      -> Unit
      }
    }
  }

  override fun intersectWithBorder(border: Direction) {
    when (border) {
      Direction.NORTH -> y = World.WORLD_HEIGHT
      Direction.EAST  -> x = 0.0
      Direction.SOUTH -> y = 0.0
      Direction.WEST  -> x = World.WORLD_WIDTH
    }
  }

  private fun shoot() {
    if (shootingCooldown <= 0.0) {
      val projectile = Projectile.create(x, y, rotation)
      projectile.accelerate(2.0)
      world.projectiles.add(projectile)
      shootingCooldown = 25.0
    }
  }

  companion object {
    private var playerNumber = 0
    fun create(x: Double, y: Double): PlayerShip {
      return PlayerShip(getImage(playerNumber++), x, y)
    }

    private fun getImage(player: Int): AnimatedImage =
        when (player) {
          0, 1 -> AnimatedImage((1..4).map { "images/playerShip_dd_$it.gif" }.map { Image(it) }, 0.100)
          else -> AnimatedImage((0..5).map { "images/ufo_$it.png" }.map { Image(it) }, 0.100)
        }

  }
}
