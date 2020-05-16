package galacticCombat.configs

import com.almasb.fxgl.dsl.getAssetLoader
import galacticCombat.entities.controller.LevelControllerComponent
import galacticCombat.level.json.LevelData
import galacticCombat.moddable.towerConfig.TowerConfiguration
import galacticCombat.services.Files
import galacticCombat.services.getFileService
import galacticCombat.ui.InfoPanel
import galacticCombat.utils.parseJson

object TowerConfigVar : GameVar<TowerConfiguration> {
  override val id = "Tower Config Variable"

  fun initialize() {
    val towerConfigsText = getAssetLoader().getStream(Files.TOWER_CONFIG_FILE.path).bufferedReader().readText()
    val moddedtowerConfigsText = getFileService().getLocalFileContent(Files.LOCAL_TOWER_CONFIG_FILE.path)
    val towerConfigs: TowerConfiguration = parseJson(moddedtowerConfigsText ?: towerConfigsText)
    set(towerConfigs)
  }
}

object LevelDataVar : GameVar<LevelData> {
  override val id = "Level Data Variable"
}

object InfoPanelVar : GameVar<InfoPanel> {
  override val id = "Info Panel Variable"

  fun initialize() = set(InfoPanel())
}

object LevelController : GameVar<LevelControllerComponent> {
  override val id = "Level Controller Component"
}

/**
 * All [GameVarsInt] will be initialized before loading the level.
 */
enum class GameVarsInt(val initial: Int) : IntGameVar {
  ALIVE_INVADERS(0),
  SCORE(0),
  WAVE_INDEX(0)
  ;

  override val id: String = name
}

enum class GameVarsBoolean(val initial: Boolean) : BooleanGameVar {
  ALL_ENEMIES_SPAWNED(false),
  ;

  override val id: String = name
}

/**
 * [LevelGameVars] are initialized from the level data file.
 */
enum class LevelGameVars : IntGameVar {
  GOLD,
  EXPERIENCE,
  HEALTH
  ;

  override val id: String = name
}