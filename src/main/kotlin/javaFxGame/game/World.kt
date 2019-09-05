package javaFxGame.game

class World {

  val ships: MutableList<Actor> = arrayListOf()
  val meteors: MutableList<Actor> = arrayListOf()
  val projectiles: MutableList<Actor> = arrayListOf()

  val players: MutableList<Player> = arrayListOf()
  val computer: MutableList<KI> = arrayListOf()

  val actors: List<MutableList<Actor>> = listOf(ships, meteors, projectiles)

  init {
    world = this

    spawnSoloPlayer()
    spawnKI()
    spawnMeteor()
  }

  fun runActors(time: Double) {
    world.actors.forEach { actorList ->
      actorList.forEach { actor ->
        actor.act(time)
      }
    }
    world.removeMarkedDeath()
  }

  private fun spawnKI() {
    val ship = PlayerShip.create(WORLD_WIDTH / 2, WORLD_HEIGHT)
    ship.rotate(-90.0)
    ship.accelerate(0.2)
    val ki = KI(ship)
    ships.add(ship)
    computer.add(ki)
  }

  private fun spawnSoloPlayer() {
    val ship = PlayerShip.create(WORLD_WIDTH / 2, WORLD_HEIGHT)
    ship.rotate(-90.0)
    ship.accelerate(0.1)
    val player = Player(ship, Controls.SOLO_PLAYER)
    ships.add(ship)
    players.add(player)
  }

  private fun spawnMeteor() {
    val meteor = Meteor.create(WORLD_WIDTH / 2, 0.0)
    meteor.rotate(90.0)
    meteor.accelerate(0.2)
    meteors.add(meteor)
  }

  private fun removeMarkedDeath() {
    actors.forEach { actorList ->
      actorList.withIndex().reversed().filter { (_, actor) ->
        actor.markedForRemoval
      }.forEach { (index, actor) ->
        actorList.removeAt(index)
        actor.whenRemoved()
      }
    }
  }

  companion object {
    const val WORLD_WIDTH = 400.0
    const val WORLD_HEIGHT = 400.0
    lateinit var world: World
  }
}