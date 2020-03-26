package galacticCombat.level

import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import galacticCombat.configs.AppConfig
import galacticCombat.configs.InfoPanelVar
import galacticCombat.configs.LevelController
import galacticCombat.configs.LevelDataVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.controller.LevelControllerComponent
import galacticCombat.entities.controller.LevelControllerFactory
import galacticCombat.entities.path.PathFactory
import galacticCombat.level.json.LevelData
import galacticCombat.ui.InfoPanel
import galacticCombat.utils.loadJson
import java.net.URL

class GalacticCombatLevelLoader : LevelLoader {

  override fun load(url: URL, world: GameWorld): Level {
    val data = loadJson<LevelData>(url)
    //todo sanity check of read data
    data.setGameVars()
    LevelDataVar.set(data)
    InfoPanelVar.set(InfoPanel())

    // Initialize Controller
    val controller = LevelControllerFactory.create(data)
    LevelController.set(controller.getComponent(LevelControllerComponent::class.java))

    // Initialize Path
    val paths = PathFactory.createPath()

    val entities = listOf(controller) + paths
    return Level(AppConfig.WIDTH, AppConfig.HEIGHT, entities)
  }

}

fun LevelData.setGameVars() {
  LevelGameVars.values().forEach { levelVar ->
    val initial = when (levelVar) { //using 'when' to make sure all values are initialized
      LevelGameVars.GOLD       -> settings.initialGold
      LevelGameVars.EXPERIENCE -> settings.initialExperience
      LevelGameVars.HEALTH     -> settings.initialHealth
    }
    levelVar.set(initial)
  }
}