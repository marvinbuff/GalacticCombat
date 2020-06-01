package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class LevelData(
  val title: String,
  val settings: Settings,
  val isEditable: Boolean,
  val waves: MutableList<Wave>,
  val paths: MutableList<Path>
) {
  fun getPathById(id: String): Path = paths.first { it.id == id }

  fun changeInvaderSpawnTime(waveIndex: Int, args: InvaderSpawnArgs, newTime: Double): InvaderSpawnArgs {
    val oldWave = waves[waveIndex]
    val newInvaders = oldWave.invaders.toMutableList()
    val successfullyRemoved = newInvaders.remove(args)
    check(successfullyRemoved) { "Failed to remove Invader $args from $newInvaders." }
    val newInvader = InvaderSpawnArgs(newTime, args.args)
    newInvaders.add(newInvader)
    newInvaders.sortBy { it.time }
    waves[waveIndex] = Wave(newInvaders, oldWave.isBoss)
    return newInvader
  }
}