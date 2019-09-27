package galacticCombat.configs

import com.almasb.fxgl.dsl.getGameState

enum class GameVars(val initial: Int) {
  // Background
  ENEMIES_TO_SPAWN(3),
  ALIVE_ENEMIES(0),

  // Shown to the Player
  GOLD(600),
  EXPERIENCE(100),
  HEALTH(3),
  SCORE(0)
  ;

  val id: String = name

  fun increment(increment: Int) {
    getGameState().increment(name, increment)
  }

  fun get(): Int = getGameState().getInt(name)

  companion object {
    fun getVarsInTopBar(): List<GameVars> {
      return listOf(GOLD, EXPERIENCE, HEALTH, SCORE, ENEMIES_TO_SPAWN, ALIVE_ENEMIES)
    }
  }
}