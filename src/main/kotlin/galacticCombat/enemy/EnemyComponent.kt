package galacticCombat.enemy

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import galacticCombat.GalacticCombatApp
import galacticCombat.event.EnemyReachedGoalEvent
import javafx.geometry.Point2D

class EnemyComponent : Component() {
  private lateinit var wayPoints: List<Point2D>
  private lateinit var nextWayPoint: Point2D
  private var wayPointIndex: Int = 0

  override fun onAdded() {
    wayPoints = (FXGL.getApp() as GalacticCombatApp).waypoints
    nextWayPoint = wayPoints[wayPointIndex]
  }

  override fun onUpdate(tpf: Double) {
    val speed = tpf * 60 * 2

    val velocity = nextWayPoint.subtract(entity.position).normalize().multiply(speed)
    entity.translate(velocity)

    if (nextWayPoint.distance(entity.position) < speed) {
      entity.position = nextWayPoint
      wayPointIndex++
      if (wayPoints.size <= wayPointIndex) {
        FXGL.getEventBus().fireEvent(EnemyReachedGoalEvent(entity))
      } else {
        nextWayPoint = wayPoints[wayPointIndex]
      }
    }
  }

}