package javaFxGame.game

class Player(playerShip: IControllable, private val controls: Controls) : IControllable by playerShip {

  // score, money, etc.

  fun input(input: Collection<String>) {
    keysPressed(input, controls)
  }

}