package galacticCombat.bullet

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.BULLET_SPAWN_ID
import galacticCombat.EntityType

@Suppress("unused")
class BulletFactory : EntityFactory {


  /**
   * Spawns a Bullet.
   * SpawnData [data] must contain the following entries:
   * - [BulletData.id]
   * - "target"
   */
  @Spawns(BULLET_SPAWN_ID)
  fun spawnBullet(data: SpawnData): Entity {
    val bulletData: BulletData = data.get(BulletData.id)
    val target: Entity = data.get("target")

    return entityBuilder().type(EntityType.BULLET)
        .view(bulletData.texture)
      .from(data)
      .scale(0.5, 0.5)
        .with(BulletComponent(target, bulletData))
      .build()
  }


}