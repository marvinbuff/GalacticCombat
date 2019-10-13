package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.GameVars
import galacticCombat.entities.bullet.BulletData
import galacticCombat.entities.bullet.BulletEffect
import galacticCombat.entities.bullet.BulletEffectType
import galacticCombat.events.InvaderEvents
import galacticCombat.utils.toPoint
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D
import kotlin.math.max

class InvaderComponent(val data: InvaderData) : Component() {
  private lateinit var nextWayPoint: Point2D
  private lateinit var lastWayPoint: Point2D
  private var wayPointIndex: Int = 1 // we skip index 0 as it spawns there
  private lateinit var projectile: ProjectileComponent
  var health: SimpleDoubleProperty = SimpleDoubleProperty(data.maxHealth)

  private val poisonEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()
  private val slowEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()

  //region -------------------- Inherited Members ------------------------

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    nextWayPoint = data.wayPoints[wayPointIndex].toPoint()
    lastWayPoint = data.wayPoints[wayPointIndex - 1].toPoint()
    projectile = ProjectileComponent(Point2D(0.0, 0.0), data.baseSpeed.speed)
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

  //endregion
  //region -------------------- Public Members ------------------------

  /**
   *  A bullet hitting an invader registers here to apply its damage and effect.
   */
  fun hitWithBullet(bullet: BulletData) {
    val effectiveArmour = max((data.armour - bullet.penetration), 0.0)
    val effectiveDmg = max((bullet.damage - effectiveArmour), 0.0)
    if (effectiveDmg == 0.0) return // Effects are not applied if no damage is done
    health.value -= effectiveDmg
    val effect = bullet.effect
    val effectWithTime = getGameTimer().now to effect
    when (effect.type) {
      BulletEffectType.POISON -> poisonEffects += effectWithTime
      BulletEffectType.SLOW   -> slowEffects += effectWithTime
      BulletEffectType.NONE   -> { /* do nothing */
      }
    }
  }

  /**
   * Returns an estimation of how much progression the invader has towards its goal.
   */
  fun getProgress(): Double {
    val progress = (wayPointIndex - 1) * 10000.0 //each wayPoint is closer than 10'000 pixel to each other
    val distanceFromOld = lastWayPoint.distance(entity.position)
    return progress + distanceFromOld
  }

  //endregion
  //region -------------------- Private Members ------------------------

  private fun followPath(tpf: Double) {
    projectile.direction = nextWayPoint.subtract(entity.position)

    if (isAtNextWaypoint(tpf)) {
      entity.position = nextWayPoint
      wayPointIndex++
      if (isLastWaypoint()) {
        FXGL.getEventBus().fireEvent(InvaderEvents(InvaderEvents.INVADER_REACHED_GOAL, this))
      } else {
        lastWayPoint = nextWayPoint
        nextWayPoint = data.wayPoints[wayPointIndex].toPoint()
      }
    }
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.position) < projectile.speed * tpf
  private fun isLastWaypoint() = data.wayPoints.size <= wayPointIndex

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
    if (slowEffects.isEmpty()) projectile.speed = data.baseSpeed.speed
    else projectile.speed = (slowEffects.map { it.second }.minBy { it.amount }?.amount ?: 1.0) * data.baseSpeed.speed
  }

  private fun checkHealth() {
    if (health.doubleValue() <= 0.0) {
      FXGL.getEventBus().fireEvent(InvaderEvents(InvaderEvents.INVADER_KILLED, this))
    }
  }

  //endregion

  companion object {
    val center = (25 / 2.0).toPoint()
  }
}