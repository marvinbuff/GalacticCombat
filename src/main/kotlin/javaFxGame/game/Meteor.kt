package javaFxGame.game

import javafx.scene.image.Image

class Meteor(image: AnimatedImage, x: Double, y: Double) : Actor(image, x, y) {


  companion object {
    // add different meteor types here.
    fun create(x: Double, y: Double): Meteor {
      return Meteor(getImage(), x, y)
    }

    private fun getImage(): AnimatedImage =
        AnimatedImage((1..1).map { "images/meteor.png" }.map { Image(it) }, 0.100)

  }
}