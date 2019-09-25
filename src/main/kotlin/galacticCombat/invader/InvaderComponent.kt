package galacticCombat.invader

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.entity.component.Component
import galacticCombat.GalacticCombatApp
import galacticCombat.GameVars
import galacticCombat.bullet.BulletData
import galacticCombat.bullet.BulletEffect
import galacticCombat.bullet.BulletEffectType
import galacticCombat.event.InvaderEvents
import galacticCombat.toPoint
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D

class InvaderComponent(val data: InvaderData) : Component() {
  private lateinit var wayPoints: List<Point2D>
  private lateinit var nextWayPoint: Point2D
  private lateinit var lastWayPoint: Point2D
  private lateinit var projectile: ProjectileComponent
  private var wayPointIndex: Int = 1 // we skip index 0 as it spawns there
  var health: SimpleDoubleProperty = SimpleDoubleProperty(data.maxHealth)

  private val poisonEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()
  private val slowEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    wayPoints = (FXGL.getApp() as GalacticCombatApp).waypoints
    nextWayPoint = wayPoints[wayPointIndex]
    lastWayPoint = wayPoints[wayPointIndex - 1]
    projectile = ProjectileComponent(Point2D(0.0, 0.0), data.baseSpeed)
    entity.addComponent(projectile)

    GameVars.ALIVE_ENEMIES.increment(+1)
  }

  override fun onUpdate(tpf: Double) {
    checkHealth()

    followPath(tpf)

    // Effects
    timeoutEffects()
    sufferPoison(tpf)
    sufferSlow()
  }

  override fun onRemoved() {
    GameVars.ALIVE_ENEMIES.increment(-1)
  }

  fun inflictDamage(bullet: BulletData) {
    health.value -= bullet.damage
    val effect = bullet.effect
    val effectWithTime = getGameTimer().now to effect
    when (effect.type) {
      BulletEffectType.POISON -> poisonEffects += effectWithTime
      BulletEffectType.SLOW   -> slowEffects += effectWithTime
      BulletEffectType.NONE   -> { /* do nothing */
      }
    }
  }

  fun getProgress(): Double {
    val progress = (wayPointIndex - 1) * 10000.0 //each wayPoint is closer than 10'000 pixel to each other
    val distanceFromOld = lastWayPoint.distance(entity.position)
    return progress + distanceFromOld
  }

  private fun followPath(tpf: Double) {
    projectile.direction = nextWayPoint.subtract(entity.position)

    if (isAtNextWaypoint(tpf)) {
      entity.position = nextWayPoint
      wayPointIndex++
      if (isLastWaypoint()) {
        FXGL.getEventBus().fireEvent(InvaderEvents(entity, data.damage))
      } else {
        lastWayPoint = nextWayPoint
        nextWayPoint = wayPoints[wayPointIndex]
      }
    }
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.position) < projectile.speed * tpf
  private fun isLastWaypoint() = wayPoints.size <= wayPointIndex

  private fun timeoutEffects() {
    val now = getGameTimer().now
    listOf(poisonEffects, slowEffects).forEach { effectList ->
      effectList.removeIf { now > it.first + it.second.duration }
    }
  }

  private fun sufferPoison(tpf: Double) {
    poisonEffects.forEach {
      health.value -= it.second.amount * tpf
    }
  }

  private fun sufferSlow() {
    if (slowEffects.isEmpty()) projectile.speed = data.baseSpeed
    else projectile.speed = (slowEffects.map { it.second }.minBy { it.amount }?.amount ?: 1.0) * data.baseSpeed
  }

  private fun checkHealth() {
    if (health.doubleValue() <= 0.0) {
      entity.removeFromWorld()
    }
  }

  companion object {
    val center = (25 / 2.0).toPoint()
  }
}