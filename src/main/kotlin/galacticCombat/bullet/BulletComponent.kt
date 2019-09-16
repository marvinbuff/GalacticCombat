package galacticCombat.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import galacticCombat.enemy.EnemyComponent
import galacticCombat.toPoint
import javafx.geometry.Point2D

class BulletComponent(private val target: Entity) : Component() {
  private lateinit var projectile: ProjectileComponent


  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    projectile = ProjectileComponent(Point2D(0.0, 0.0), BASE_SPEED)
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val adjustedTargetPosition = target.position.add(EnemyComponent.center.subtract(center))
    val vectorToTarget = adjustedTargetPosition.subtract(entity.position)
    projectile.direction = vectorToTarget

    if (vectorToTarget.magnitude() < projectile.speed * tpf) {
      //TODO: deal damage
      entity.removeFromWorld()
    }
  }

  companion object {
    const val BASE_SPEED = 80 * 2.0
    val center = (50/2.0).toPoint()
  }
}