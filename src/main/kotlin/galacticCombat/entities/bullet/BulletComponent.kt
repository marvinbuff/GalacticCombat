package galacticCombat.entities.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.utils.toPoint

@Required(ProjectileComponent::class)
class BulletComponent(
    private val target: Entity,
    private val data: BulletData
) : Component() {
  private val projectile: ProjectileComponent by lazy { entity.getComponent(ProjectileComponent::class.java) }

  override fun onUpdate(tpf: Double) {
    val vectorToTarget = target.anchoredPosition.subtract(entity.anchoredPosition)
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