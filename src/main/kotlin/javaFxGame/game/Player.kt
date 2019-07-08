package javaFxGame.game

class Player(private val playerShip: IControllable, private val controls: Controls) {

  // score, money, etc.

  fun input(input: Collection<String>) {
    playerShip.keysPressed(input, controls)
  }

}