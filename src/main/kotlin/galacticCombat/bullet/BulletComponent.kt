package galacticCombat.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import javafx.geometry.Point2D

class BulletComponent : Component() {
  private lateinit var target: Entity
  private lateinit var projectile: ProjectileComponent

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = Point2D(12.5, 12.5)

    projectile = ProjectileComponent(Point2D(0.0, 0.0), BASE_SPEED)
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    projectile.direction = target.position.subtract(entity.position)

    if (hitsTarget(tpf)) {
      //TODO: deal damage
      entity.removeFromWorld()
    }
  }

  // TODO already hit when in proximity
  private fun hitsTarget(tpf: Double) = target.position.distance(entity.position) < projectile.speed * tpf

  companion object {
    const val BASE_SPEED = 60 * 4.0
  }
}