package galacticCombat.invader

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.entity.component.Component
import galacticCombat.GalacticCombatApp
import galacticCombat.GameVars
import galacticCombat.bullet.BulletData
import galacticCombat.bullet.BulletEffect
import galacticCombat.bullet.BulletEffectType
import galacticCombat.event.EnemyReachedGoalEvent
import galacticCombat.toPoint
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D

class InvaderComponent(
  val maxHealth: Double = 100.0
) : Component() {
  private lateinit var wayPoints: List<Point2D>
  private lateinit var nextWayPoint: Point2D
  private lateinit var lastWayPoint: Point2D
  private lateinit var projectile: ProjectileComponent
  private var wayPointIndex: Int = 1 // we skip index 0 as it spawns there
  var health: SimpleDoubleProperty = SimpleDoubleProperty(maxHealth)

  val poisonEffects: ArrayList<BulletEffect> = arrayListOf()

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    wayPoints = (FXGL.getApp() as GalacticCombatApp).waypoints
    nextWayPoint = wayPoints[wayPointIndex]
    lastWayPoint = wayPoints[wayPointIndex - 1]
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
        lastWayPoint = nextWayPoint
        nextWayPoint = wayPoints[wayPointIndex]
      }
    }

    // Go through effects
    poisonEffects.removeIf { it.duration <= 0 }
    poisonEffects.forEach {
      val time = (tpf / 100)
      health.value -= it.amount * time
      it.duration -= time
    }
  }

  override fun onRemoved() {
    getGameState().increment(GameVars.ALIVE_ENEMIES.id, -1)
  }

  fun inflictDamage(bullet: BulletData) {
    health.value -= bullet.damage
    if (health.doubleValue() <= 0.0){
      entity.removeFromWorld()
    }
    val effect = bullet.effect
    if (effect.type == BulletEffectType.POISON) poisonEffects += effect
  }

  fun getProgress(): Double {
    val progress = (wayPointIndex - 1) * 10000.0 //each wayPoint is closer than 10'000 pixel to each other
    val distanceFromOld = lastWayPoint.distance(entity.position)
    return progress + distanceFromOld
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.position) < projectile.speed * tpf
  private fun isLastWaypoint() = wayPoints.size <= wayPointIndex

  companion object{
    const val BASE_SPEED = 30 * 2.0
    val center = (25/2.0).toPoint()
  }
}