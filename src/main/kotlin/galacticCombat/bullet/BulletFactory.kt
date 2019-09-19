package galacticCombat.bullet

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.AssetsConfig
import galacticCombat.BULLET_ID
import galacticCombat.EntityType
import galacticCombat.invader.HealthComponent
import galacticCombat.invader.InvaderComponent

@Suppress("unused")
class BulletFactory : EntityFactory {


  @Spawns(BULLET_ID)
  fun spawnEnemy(data: SpawnData): Entity {
    val invader = InvaderComponent()
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.BULLET)
      .from(data)
      .viewWithBBox(AssetsConfig.getEnemy("1.1.gif"))
      .with(CollidableComponent(true))
      .with(invader)
      .with(healthBar)
      .build()
  }


}