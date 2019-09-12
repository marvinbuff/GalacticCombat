package galacticCombat

import com.almasb.fxgl.app.ApplicationMode

//TODO instead of having one Config object it might be preferable to have different ones for each concern. Just Maybe.
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

object AssetsConfig {
  const val ENEMIES = "enemies/"
  const val TOWERS = "towers/"

  fun getEnemy(file: String): String = "$ENEMIES$file"
  fun getTower(file: String): String = "$TOWERS$file"
}