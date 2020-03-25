package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class LevelData(
    val title: String,
    val settings: Settings,
    val waves: MutableList<Wave>,
    val paths: MutableList<Path>
) {
  fun getPathById(id: String): Path = paths.first { it.id == id }

  fun changeInvaderSpawnTime(waveIndex: Int, args: InvaderSpawnArgs, newTime: Double) {
    val oldWave = waves[waveIndex]
    val newInvaders = oldWave.invaders.toMutableList()
    newInvaders.remove(args)
    newInvaders.add(InvaderSpawnArgs(newTime, args.args))
    newInvaders.sortBy { it.time }
    waves[waveIndex] = Wave(newInvaders, oldWave.isBoss)
  }
}