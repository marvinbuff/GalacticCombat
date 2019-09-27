package galacticCombat.entities.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D

class BulletComponent(
    private val target: Entity,
    private val data: BulletData
) : Component() {
  private lateinit var projectile: ProjectileComponent


  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    projectile = ProjectileComponent(Point2D(0.0, 0.0), data.bulletSpeed)
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val adjustedTargetPosition = target.position.add(InvaderComponent.center.subtract(center))
    val vectorToTarget = adjustedTargetPosition.subtract(entity.position)
    projectile.direction = vectorToTarget

    if (vectorToTarget.magnitude() < projectile.speed * tpf) {
      if (target.isActive) {
        target.getComponent(InvaderComponent::class.java).hitWithBullet(data)
      }
      entity.removeFromWorld()
    }
  }

  companion object {
    val center = (50 / 2.0 / 2.0).toPoint()
  }
}