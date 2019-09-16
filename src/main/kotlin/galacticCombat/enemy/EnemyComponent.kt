package galacticCombat.enemy

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.component.Component
import galacticCombat.GalacticCombatApp
import galacticCombat.event.EnemyReachedGoalEvent
import galacticCombat.toPoint
import javafx.geometry.Point2D

class EnemyComponent(
  private var health: Double = 100.0
) : Component() {
  private lateinit var wayPoints: List<Point2D>
  private lateinit var nextWayPoint: Point2D
  private lateinit var projectile: ProjectileComponent
  private var wayPointIndex: Int = 1 // we skip index 0 as it spawns there

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    wayPoints = (FXGL.getApp() as GalacticCombatApp).waypoints
    nextWayPoint = wayPoints[wayPointIndex]
    projectile = ProjectileComponent(Point2D(0.0, 0.0), BASE_SPEED)
    entity.addComponent(projectile)
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

  fun inflictDamage(damage: Double){
    health -= damage
    if (health <= 0){
      entity.removeFromWorld()
    }
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.position) < projectile.speed * tpf
  private fun isLastWaypoint() = wayPoints.size <= wayPointIndex

  companion object{
    const val BASE_SPEED = 10 * 2.0
    val center = (25/2.0).toPoint()
  }
}