package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.configs.GameVarsInt
import galacticCombat.events.InvaderEvents
import galacticCombat.moddable.towerConfig.BulletData
import galacticCombat.moddable.towerConfig.BulletEffect
import galacticCombat.moddable.towerConfig.BulletEffectType
import galacticCombat.ui.HasInfo
import galacticCombat.utils.fire
import galacticCombat.utils.toPoint
import javafx.beans.binding.StringBinding
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D
import javafx.scene.image.Image
import kotlin.math.max

@Required(ProjectileComponent::class)
class InvaderComponent(val data: InvaderData) : Component(), HasInfo {
  private lateinit var projectile: ProjectileComponent
  private lateinit var nextWayPoint: Point2D
  private lateinit var lastWayPoint: Point2D
  private var wayPointIndex: Int = 1 // we skip index 0 as invaders spawns there
  var health: SimpleDoubleProperty = SimpleDoubleProperty(data.maxHealth)

  private val poisonEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()
  private val slowEffects: ArrayList<Pair<Double, BulletEffect>> = arrayListOf()

  //region -------------------- Entity Members ------------------------

  override fun onAdded() {
    nextWayPoint = data.wayPoints[wayPointIndex].toPoint()
    lastWayPoint = data.wayPoints[wayPointIndex - 1].toPoint()

    GameVarsInt.ALIVE_INVADERS.increment(+1)
  }

  override fun onUpdate(tpf: Double) {
    checkHealth()

    followPath(tpf)

    // Effects
    timeoutEffects()
    sufferPoison(tpf)
    sufferSlow()
  }

  //endregion
  //region -- HasInfo --

  override fun getTitle(): String = "${data.args.type.title} - Lvl ${data.args.level}"

  override fun getInformation(): StringBinding {
    val formattedMax = "%.0f".format(data.maxHealth)
    val infoText = "Health: \t%.0f/$formattedMax\n" +
        "Armour: \t${"%.0f".format(data.armour)}\n" +
        "Speed: \t${data.baseSpeed.name.toLowerCase()}\n" +
        "Bounty: \t${data.bounty}"
    return health.asString(infoText)
  }

  override fun getTexture(): Image = data.texture.getRepresentative()

  override fun activate() = Unit

  override fun deactivate() = Unit

  //endregion
  //region -- Public Members --

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
      BulletEffectType.CHAIN  -> TODO()
      BulletEffectType.NONE   -> { /* do nothing */
      }
    }
  }

  /**
   * Returns an estimation of how much progression the invader has towards its goal.
   */
  fun getProgress(): Double {
    val progress = (wayPointIndex - 1) * 10000.0 //each wayPoint is closer than 10'000 pixel to each other
    val distanceFromOld = lastWayPoint.distance(entity.anchoredPosition)
    return progress + distanceFromOld
  }

  //endregion
  //region -------------------- Private Members ------------------------

  private fun followPath(tpf: Double) {
    projectile.direction = nextWayPoint.subtract(entity.anchoredPosition)

    if (isAtNextWaypoint(tpf)) {
      wayPointIndex++
      if (isLastWaypoint()) {
        InvaderEvents(InvaderEvents.INVADER_REACHED_GOAL, this).fire()
      } else {
        lastWayPoint = nextWayPoint
        nextWayPoint = data.wayPoints[wayPointIndex].toPoint()
      }
    }
  }

  private fun isAtNextWaypoint(tpf: Double) = nextWayPoint.distance(entity.anchoredPosition) < projectile.speed * tpf
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
    if (slowEffects.isEmpty()) projectile.speed = data.baseSpeed.value
    else projectile.speed = (slowEffects.map { it.second }.minBy { it.amount }?.amount ?: 1.0) * data.baseSpeed.value
  }

  private fun checkHealth() {
    if (health.doubleValue() <= 0.0) {
      health.value = 0.0
      InvaderEvents(InvaderEvents.INVADER_KILLED, this).fire()
    }
  }

  //endregion

  companion object {
    val center = (25 / 2.0).toPoint()
  }
}