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

//  fun changeInvaderSpawnTime(wave: Wave, args: InvaderArgs, prevTime: Double, newTime: Double) {
//    val index = waves.indexOf(wave)
//    val oldWave = waves[index]
//    val newInvaders = oldWave.invaders.toMutableList()
//    newInvaders.remove(prevTime to args)
//    newInvaders.add(newTime.args)
//    val newWave = oldWave
//    waves[index] = newWave
//  }
}