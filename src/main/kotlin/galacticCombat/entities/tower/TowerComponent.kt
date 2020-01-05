package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import galacticCombat.entities.BULLET_SPAWN_ID
import galacticCombat.entities.EntityType
import galacticCombat.entities.bullet.BulletData
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D
import javafx.scene.transform.Rotate

open class TowerComponent(towerData: TowerData) : Component() {
  private lateinit var projectile: ProjectileComponent
  private var reloadingTime: Double = 0.0
  private val bullet = towerData.bulletData

  override fun onAdded() {
    entity.transformComponent.rotationOrigin = center

    projectile = ProjectileComponent(Point2D(0.0, 0.0), 0.1)
    entity.addComponent(projectile)
  }

  override fun onUpdate(tpf: Double) {
    val closestInvader = getGameWorld()
        .getEntitiesByType(EntityType.INVADER)
        .filter { other -> entity.anchoredPosition.distance(other.anchoredPosition) < bullet.range }
      .maxBy { it.getComponent(InvaderComponent::class.java).getProgress() }
    closestInvader?.let {
      projectile.direction = Rotate.rotate(-45.0, 0.0, 0.0).transform(it.anchoredPosition.subtract(entity.anchoredPosition))
      if (reloadingTime <= 0.0) {
        shoot(closestInvader)
        reloadingTime = bullet.attackDelay
      } else {
        reloadingTime -= tpf
      }
    }
  }

  private fun shoot(target: Entity) {
    getGameWorld().spawn(
      BULLET_SPAWN_ID,
        SpawnData(entity.anchoredPosition)
          .put("target", target).put(BulletData.id, bullet)
    )
  }

  companion object {
    val center = (38/2.0).toPoint()
  }
}