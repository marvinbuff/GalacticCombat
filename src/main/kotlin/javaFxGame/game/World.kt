package javaFxGame.game

class World {

  val actors: MutableList<Actor> = arrayListOf()
  val players: MutableList<Player> = arrayListOf()

  init {
    world = this
  }

  fun spawnSoloPlayer() {
    val ship = PlayerShip.create(WORLD_WIDTH / 2, WORLD_HEIGHT)
    ship.rotate(-90.0)
    ship.accelerate(0.1)
    val player = Player(ship, Controls.SOLO_PLAYER)
    actors.add(ship)
    players.add(player)
  }

  fun spawnMeteor() {
    val meteor = Meteor.create(WORLD_WIDTH / 2, 0.0)
    meteor.rotate(90.0)
    meteor.accelerate(0.2)
    actors.add(meteor)
  }

  companion object {
    const val WORLD_WIDTH = 400.0
    const val WORLD_HEIGHT = 400.0
    lateinit var world: World
  }
}