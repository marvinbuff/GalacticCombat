package galacticCombat.tower

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import galacticCombat.EntityType
import javafx.geometry.Point2D
import javafx.scene.transform.Rotate

class TowerComponent : Component() {
  private lateinit var projectile: ProjectileComponent

  override fun onAdded() {
    projectile = ProjectileComponent(Point2D(0.0, 0.0), 0.1) //TODO replace by unmovable Component
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val closestEnemy = getGameWorld().getEntitiesByType(EntityType.ENEMY).maxBy { other -> entity.position.distance(other.position) }
    closestEnemy?.let {
      projectile.direction = Rotate.rotate(-45.0, 0.0, 0.0).transform(it.position.subtract(entity.position))
    }
  }


  companion object {
    const val SHOOT_CD = 3.0
  }
}