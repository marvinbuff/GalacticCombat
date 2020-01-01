package galacticCombat.configs

import com.almasb.fxgl.app.ApplicationMode

object AppConfig {
  const val VERSION = "0.1"
  val MODE = ApplicationMode.DEVELOPER

  const val WIDTH = 800
  const val HEIGHT = 500
  const val TITLE = "Galactic Combat"
  const val TRICKLE_RATE = 1.0
}

object GameConfig {
  const val WORLD_HEIGHT = AppConfig.HEIGHT - UIConfig.TOPBAR_HEIGHT
}

object UIConfig {
  const val TOPBAR_HEIGHT = 100
}

//TODO Refactor the achievements and put into own file
object AchievementConfig {
  const val GOLD_ACHIEVEMENT_1 = 100
}

