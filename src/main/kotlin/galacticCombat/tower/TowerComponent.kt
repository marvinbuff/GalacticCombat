package galacticCombat.tower

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import galacticCombat.BULLET_ID
import galacticCombat.EntityType
import galacticCombat.bullet.BulletComponent
import galacticCombat.invader.InvaderComponent
import galacticCombat.toPoint
import javafx.geometry.Point2D
import javafx.scene.transform.Rotate

open class TowerComponent(val towerData: TowerData) : Component(){
  private lateinit var projectile: ProjectileComponent
  private var reloadingTime: Double = 0.0

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    projectile = ProjectileComponent(Point2D(0.0, 0.0), 0.1)
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val closestInvader = getGameWorld()
      .getEntitiesByType(EntityType.INVADER)
      .filter { other -> entity.position.distance(other.position) < towerData.range }
      .maxBy { it.getComponent(InvaderComponent::class.java).getProgress() }
    closestInvader?.let {
      projectile.direction = Rotate.rotate(-45.0, 0.0, 0.0).transform(it.position.subtract(entity.position))
      if (reloadingTime <= 0.0) {
        shoot(closestInvader)
        reloadingTime = towerData.attackDelay
      } else {
        reloadingTime -= tpf
      }

    }
  }

  private fun shoot(target: Entity) {
    getGameWorld().spawn(
      BULLET_ID,
      SpawnData(entity.position.add(center.subtract(BulletComponent.center)))
        .put("target", target)
    )
  }

  companion object {
    val center = (38/2.0).toPoint()
  }
}