package galacticCombat

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import java.net.URL

class GalacticCombatLevelLoader : LevelLoader {
  override fun load(url: URL, world: GameWorld): Level {
    val lines = url.openStream().bufferedReader().readLines()

    val entities = listOf<Entity>()
    // Create Timer
    // Add Entities at specified time to Timer
    // Calibrate initial settings: gold, hp, pre-built towers, time per wave, etc

    return Level(AppConfig.WIDTH, AppConfig.HEIGHT, entities)
  }

  private fun List<String>.readLevelData(): LevelData {
    val map = this.readMap()
    return LevelData(map.getValue("title"), enemies = listOf(Entity() to 2.3))
  }

  private fun List<String>.readMap(): Map<String, String> {
    val del = ":"
    val map = this.asSequence()
      .filter { it.contains(del) }
      .map { it.split(del) }
      .filter { size == 2 }
      .map { it[0] to it[1] }
      .toMap()
    return map
  }
}

private data class LevelData(
  val title: String,
  val initialGold: Int = 100,
  val initialHP: Int = 10,
  val timePerWave: Double = 60.0,
  val enemies: List<Pair<Entity, Double>>
)