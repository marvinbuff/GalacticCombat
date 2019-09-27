package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.FXGL
import galacticCombat.GalacticCombatApp
import javafx.geometry.Point2D

data class InvaderData(
  val texture: String,
  val maxHealth: Double,
  val baseSpeed: Speed,
  val armour: Double,
  val xp: Int,
  val bounty: Int = 100,
  val damage: Int = 1,
  val wayPoints: List<Point2D> = (FXGL.getApp() as GalacticCombatApp).waypoints
) {
  companion object {
    const val id = "InvaderData"
  }
}

enum class Speed(val speed: Double) {
  FAST(100.0),
  NORMAL(60.0),
  SLOW(40.0)
}