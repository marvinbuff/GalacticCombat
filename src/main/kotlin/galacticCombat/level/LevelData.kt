package galacticCombat.level

import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.invader.InvaderType
import galacticCombat.utils.jsonSerializer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.stringify

@Serializable
data class LevelData(
    //move settings data into LevelSettingsData object
    val title: String,
    val initialGold: Int = 100,
    val trickleGold: Int = 5,
    val trickleScore: Int = 5,
    val initialHealth: Int = 10,
    val initialExperience: Int = 0,
    val timePerWave: Double = 60.0,
    val waves: List<Wave>,
    val paths: List<Path>
) {
  fun setGameVars() {
    LevelGameVars.values().forEach { levelVar ->
      val initial = when (levelVar) { //using 'when' to make sure all values are initialized
        LevelGameVars.GOLD       -> initialGold
        LevelGameVars.EXPERIENCE -> initialExperience
        LevelGameVars.HEALTH     -> initialHealth
      }
      levelVar.set(initial)
    }
  }
}

@UnstableDefault
@ImplicitReflectionSerializer
fun main() {
  println("Experiment Level File:")
  val path = Path("main_path", listOf(50 to 150, 150 to 350, 550 to 350, 130 to 120))
  val invaders = listOf(
      InvaderArgs(InvaderType.COMMON, 1),
      InvaderArgs(InvaderType.ACCELERATED, 2),
      InvaderArgs(InvaderType.REINFORCED, 3)
  )
  val spawnTimes = listOf(1, 2, 4).map { it.toDouble() }
  val wave = Wave(spawnTimes.zip(invaders))
  val data = LevelData("title", paths = listOf(path), waves = listOf(wave))
  val json = jsonSerializer.stringify(data)
  println(json)
}
