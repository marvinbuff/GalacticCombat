package galacticCombat.configs

import galacticCombat.level.LevelData

val varsInTopBar: List<IntGameVar> = listOf(LevelGameVars.GOLD, LevelGameVars.EXPERIENCE, LevelGameVars.HEALTH, GameVars.SCORE, GameVars.ENEMIES_TO_SPAWN, GameVars.ALIVE_ENEMIES)

object LevelDataVar : GameVar<LevelData> {
  override val id = "Level Data Variable"
}

/**
 * All [GameVars] will be initialized before loading the level.
 */
enum class GameVars(val initial: Int) : IntGameVar {
  ENEMIES_TO_SPAWN(2),
  ALIVE_ENEMIES(0),
  SCORE(0)
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