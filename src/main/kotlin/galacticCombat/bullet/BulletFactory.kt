package galacticCombat.bullet

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.AssetsConfig
import galacticCombat.BULLET_ID
import galacticCombat.EntityType

@Suppress("unused")
class BulletFactory : EntityFactory {


  /**
   * Spawns a Bullet.
   * SpawnData [data] must have a set "target".
   */
  @Spawns(BULLET_ID)
  fun spawnBullet(data: SpawnData): Entity {
    return entityBuilder().type(EntityType.BULLET)
      .view(AssetsConfig.get("beeper.png"))
      .from(data)
      .scale(0.5, 0.5)
      .with(BulletComponent(data.get("target") as Entity))
      .build()
  }


}