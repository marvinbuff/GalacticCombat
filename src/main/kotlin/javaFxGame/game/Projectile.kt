package javaFxGame.game

import javaFxGame.game.World.Companion.world
import javafx.scene.image.Image

class Projectile(image: AnimatedImage, x: Double, y: Double, rotation: Double) : Actor(image, x, y, rotation) {

  override fun act(time: Double) {
    super.act(time)
    world.meteors.forEach { meteor ->
      if (!meteor.markedForRemoval && this.intersectWith(meteor)) {
        meteor.markForRemoval()
        this.markForRemoval()
      }
    }
  }

  override fun intersectWithBorder(border: Direction) {
    markForRemoval()
  }


  companion object {
    // add different meteor types here.
    fun create(x: Double, y: Double, rotation: Double): Projectile {
      return Projectile(getImage(), x, y, rotation)
    }

    private fun getImage(): AnimatedImage =
        AnimatedImage((1..1).map { "images/beeper.gif" }.map { Image(it) }, 0.100)

  }
}