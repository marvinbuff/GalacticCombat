package galacticCombat.level

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import galacticCombat.configs.AppConfig
import galacticCombat.configs.LevelGameVars
import galacticCombat.utils.loadJson
import kotlinx.serialization.Serializable
import java.net.URL


class GalacticCombatLevelLoader : LevelLoader {

  override fun load(url: URL, world: GameWorld): Level {
    val data = loadJson<LevelData>(url)
    data.setGameVars()

    val entities = listOf<Entity>()
    // Create Timer
    // Add Entities at specified time to Timer
    // Calibrate initial settings: gold, hp, pre-built towers, time per wave, etc

    return Level(AppConfig.WIDTH, AppConfig.HEIGHT, entities)
  }
}

@Serializable
data class LevelData(
    val title: String,
    val initialGold: Int = 100,
    val initialHealth: Int = 10,
    val initialExperience: Int = 0,
    val timePerWave: Double = 60.0
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

  //Todo set game configs object with stuff like passive gold income
}