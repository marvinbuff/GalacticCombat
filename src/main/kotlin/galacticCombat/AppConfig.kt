package galacticCombat

import com.almasb.fxgl.app.ApplicationMode

//TODO maybe use a config folder with each object in a file.

object AppConfig {
  const val VERSION = "0.1"
  val MODE = ApplicationMode.DEVELOPER

  const val WIDTH = 800
  const val HEIGHT = 600
  const val TITLE = "Galactic Combat"
}

object AchievementConfig {
  const val GOLD_ACHIEVEMNENT_1 = 100
}

enum class GameVars(val initial: Int) {
  // Background
  ENEMIES_TO_SPAWN(2),
  ALIVE_ENEMIES(0),

  // Shown to the Player
  GOLD(600),
  HEALTH(10),
  SCORE(0)
  ;

  val id: String = name

  companion object{
    fun getVarsInTopBar(): List<GameVars>{
      return listOf(GOLD, HEALTH, SCORE, ENEMIES_TO_SPAWN, ALIVE_ENEMIES)
    }
  }
}

object AssetsConfig {
  const val ENEMIES = "enemies/"
  const val TOWERS = "towers/"

  fun getEnemy(file: String): String = "$ENEMIES$file"
  fun getTower(file: String): String = "$TOWERS$file"
  fun get(file: String): String = file
}