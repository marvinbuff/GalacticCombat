package galacticCombat.entities.bullet

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.BULLET_SPAWN_ID
import galacticCombat.entities.EntityType
import galacticCombat.entities.generic.HittableComponent
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.moddable.towerConfig.BulletData
import galacticCombat.utils.position
import javafx.geometry.Point2D

@Suppress("unused")
class BulletFactory : EntityFactory {

  /**
   * Spawns a Bullet.
   * @param data Needs to contain the following entries:
   * - [BulletData.id]
   * - "target"
   */
  @Spawns(BULLET_SPAWN_ID)
  fun spawnBullet(data: SpawnData): Entity {
    val bulletData: BulletData = data.get(BulletData.id)
    val target: HittableComponent = data.get("target")

    val projectile = ProjectileComponent(Point2D(0.0, 0.0), bulletData.bulletSpeed)
    val bulletComponent = BulletComponent(target, bulletData)

    return entityBuilder().setTypeAdvanced(EntityType.BULLET)
      .view(bulletData.texture)
      .atAnchored(BulletComponent.center, data.position)
      .rotationOrigin(BulletComponent.center)
      .scale(0.5, 0.5)
      .with(projectile)
      .with(bulletComponent)
      .build()
  }

  companion object {
    //todo put "target" as an id

    fun spawnBullet(position: Point2D, data: BulletData, target: HittableComponent) =
      getGameWorld().spawn(
        BULLET_SPAWN_ID,
        SpawnData(position)
          .put("target", target).put(BulletData.id, data)
      )

  }
}