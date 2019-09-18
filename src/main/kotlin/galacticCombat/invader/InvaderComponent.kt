package galacticCombat.invader

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.entity.component.Component
import galacticCombat.GalacticCombatApp
import galacticCombat.GameVars
import galacticCombat.event.EnemyReachedGoalEvent
import galacticCombat.toPoint
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D

class InvaderComponent(
  val maxHealth: Double = 100.0
) : Component() {
  private lateinit var wayPoints: List<Point2D>
  private lateinit var nextWayPoint: Point2D
  private lateinit var projectile: ProjectileComponent
  private var wayPointIndex: Int = 1 // we skip index 0 as it spawns there
  var health: SimpleDoubleProperty = SimpleDoubleProperty(maxHealth)

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    wayPoints = (FXGL.getApp() as GalacticCombatApp).waypoints
    nextWayPoint = wayPoints[wayPointIndex]
    projectile = ProjectileComponent(Point2D(0.0, 0.0), BASE_SPEED)
    entity.addComponent(projectile)

    getGameState().increment(GameVars.ALIVE_ENEMIES.id, +1)
  }

  override fun onUpdate(tpf: Double) {
    projectile.direction = nextWayPoint.subtract(entity.position)

    if (isAtNextWaypoint(tpf)) {
      entity.position = nextWayPoint
      wayPointIndex++
      if (isLastWaypoint()) {
        FXGL.getEventBus().fireEvent(EnemyReachedGoalEvent(entity))
      } else {
        nextWayPoint = wayPoints[wayPointIndex]
      }
    }
  }

  override fun onRemoved() {
    getGameState().increment(GameVars.ALIVE_ENEMIES.id, -1)
  }

  fun inflictDamage(damage: Double){
    health.value -= damage
    if (health.doubleValue() <= 0.0){
      entity.removeFromWorld()
    }
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.position) < projectile.speed * tpf
  private fun isLastWaypoint() = wayPoints.size <= wayPointIndex

  companion object{
    const val BASE_SPEED = 30 * 2.0
    val center = (25/2.0).toPoint()
  }
}