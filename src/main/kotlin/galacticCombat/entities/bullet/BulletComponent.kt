package galacticCombat.entities.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.entities.generic.HittableComponent
import galacticCombat.moddable.towerConfig.BulletData
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D

@Required(ProjectileComponent::class)
class BulletComponent(
  private val target: HittableComponent,
  private val data: BulletData
) : Component() {
  private lateinit var projectile: ProjectileComponent
  private val targetEntity: Entity = target.entity
  private val initialTargetPosition: Point2D = targetEntity.position

  override fun onUpdate(tpf: Double) {
    val vectorToTarget = getTargetPosition().subtract(entity.anchoredPosition)
    projectile.direction = vectorToTarget

    if (vectorToTarget.magnitude() < projectile.speed * tpf) {
      //todo implement explosion here: check radius of targeting mode
      if (targetEntity.isActive) {
        target.hitWithBullet(data)
      }
      entity.removeFromWorld()
    }
  }

  private fun getTargetPosition(): Point2D {
    return if (data.targetingMode.tracking) {
      targetEntity.anchoredPosition
    } else {
      initialTargetPosition
    }
  }

  companion object {
    val center = (50 / 2.0 / 2.0).toPoint()
  }
}