package galacticCombat.configs

import galacticCombat.level.json.LevelData

val varsInTopBar: List<IntGameVar> = listOf(LevelGameVars.GOLD, LevelGameVars.EXPERIENCE, LevelGameVars.HEALTH, GameVarsInt.SCORE, GameVarsInt.ENEMIES_TO_SPAWN, GameVarsInt.ALIVE_INVADERS)

object LevelDataVar : GameVar<LevelData> {
  override val id = "Level Data Variable"
}

/**
 * All [GameVarsInt] will be initialized before loading the level.
 */
enum class GameVarsInt(val initial: Int) : IntGameVar {
  ENEMIES_TO_SPAWN(2),
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