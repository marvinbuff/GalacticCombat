package galacticCombat.level

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import galacticCombat.configs.AppConfig
import galacticCombat.utils.loadJson
import galacticCombat.utils.writeJson
import kotlinx.serialization.Serializable
import java.net.URL
import kotlin.system.exitProcess


class GalacticCombatLevelLoader : LevelLoader {

  override fun load(url: URL, world: GameWorld): Level {
      writeJson(url, LevelData("My New Title"))
      val data = loadJson<LevelData>(url)
      println("Read Data:\n $data")
      exitProcess(0)

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
    val initialHP: Int = 10,
    val timePerWave: Double = 60.0
)