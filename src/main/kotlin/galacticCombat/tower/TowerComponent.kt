package galacticCombat.tower

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import galacticCombat.AssetsConfig
import galacticCombat.EntityType
import galacticCombat.bullet.BulletComponent
import galacticCombat.toPoint
import javafx.geometry.Point2D
import javafx.scene.transform.Rotate

class TowerComponent : Component() {
  private lateinit var projectile: ProjectileComponent
  private var reloadingTime: Double = 0.0

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    projectile = ProjectileComponent(Point2D(0.0, 0.0), 0.1) //TODO replace by unmovable Component
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val closestEnemy = getGameWorld().getEntitiesByType(EntityType.ENEMY).maxBy { other -> entity.position.distance(other.position) }
    closestEnemy?.let {
      projectile.direction = Rotate.rotate(-45.0, 0.0, 0.0).transform(it.position.subtract(entity.position))
      if (reloadingTime <= 0.0) {
        shoot(closestEnemy)
        reloadingTime = SHOOT_CD
      } else {
        reloadingTime -= tpf
      }

    }
  }

  private fun shoot(target: Entity) {
    entityBuilder().type(EntityType.PROJECTILE)
      .at(entity.position.add(center.subtract(BulletComponent.center)))
      .view(AssetsConfig.get("beeper.png"))
      .with(BulletComponent(target))
      .buildAndAttach()
  }

  companion object {
    const val SHOOT_CD = 2.0
    val center = (38/2.0).toPoint()
  }
}