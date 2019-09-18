package galacticCombat.invader

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.AssetsConfig
import galacticCombat.ENEMY_ID
import galacticCombat.EntityType

@Suppress("unused")
class InvadorFactory : EntityFactory {


  @Spawns(ENEMY_ID)
  fun spawnEnemy(data: SpawnData): Entity {
    val invader = InvaderComponent()
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
      .from(data)
      .viewWithBBox(AssetsConfig.getEnemy("1.1.gif"))
      .with(CollidableComponent(true))
      .with(invader)
      .with(healthBar)
      .build()
  }


}