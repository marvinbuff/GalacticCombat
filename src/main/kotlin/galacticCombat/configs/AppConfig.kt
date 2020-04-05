package galacticCombat.configs

import com.almasb.fxgl.app.ApplicationMode
import galacticCombat.utils.coerceIn
import galacticCombat.utils.plus
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.paint.Color

object AppConfig {
  const val VERSION = "0.1"
  val MODE = ApplicationMode.DEVELOPER

  const val WIDTH = 800
  const val HEIGHT = 500
  const val TITLE = "Galactic Combat"
  const val TRICKLE_RATE = 1.0
}

object GameConfig {
  val WORLD_POSITION = Point2D(83.0, 54.0)
  val WORLD_DIMENSION = Point2D(523.0, 368.0)
  val WORLD_CORNER: Point2D = WORLD_POSITION + WORLD_DIMENSION

  private val worldBounds = Rectangle2D(WORLD_POSITION.x, WORLD_POSITION.y, WORLD_DIMENSION.x, WORLD_DIMENSION.y)

  fun isPointInWorld(point: Point2D): Boolean = worldBounds.contains(point)
  fun constrainPointToWorld(point: Point2D): Point2D = point.coerceIn(WORLD_POSITION, WORLD_CORNER)
}

object UIConfig {
  val LEVEL_COLOR: Color = Color.web("133a19")
  val PATH_COLOR: Color = Color.web("5d665f")
}

//TODO Refactor the achievements and put into own file
object AchievementConfig {
  const val GOLD_ACHIEVEMENT_1 = 100
}

