package galacticCombat.configs

import com.almasb.fxgl.dsl.getGameState
import galacticCombat.level.LevelData

interface GameVar<T : Any> {
  val id: String

  fun set(value: T) {
    getGameState().setValue(id, value)
  }

  fun get(): T
}

interface IntGameVar : GameVar<Int> {
  fun increment(increment: Int) {
    getGameState().increment(id, increment)
  }

  override fun get(): Int = getGameState().getInt(id)

  companion object {
    fun getVarsInTopBar(): List<IntGameVar> { //TODO not nice to have this list in super class
      return listOf(LevelGameVars.GOLD, LevelGameVars.EXPERIENCE, LevelGameVars.HEALTH, GameVars.SCORE, GameVars.ENEMIES_TO_SPAWN, GameVars.ALIVE_ENEMIES)
    }
  }
}

enum class AnyGameVars : GameVar<Any> {
  LEVEL_DATA { //Todo maybe just use global object instead?
    override fun get(): LevelData = getGameState().getObject(id)
  };

  override val id: String = name

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