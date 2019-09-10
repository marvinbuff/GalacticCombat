package galacticCombat.enemy

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.ENEMY_ID
import galacticCombat.EntityType

class EnemyFactory : EntityFactory {

  @Spawns(ENEMY_ID)
  fun spawnEnemy(data: SpawnData): Entity =
    entityBuilder().type(EntityType.SHIP)
      .from(data)
      .viewWithBBox("meteor.png")
      .with(CollidableComponent(true))
      .with(EnemyComponent())
      .build()
}