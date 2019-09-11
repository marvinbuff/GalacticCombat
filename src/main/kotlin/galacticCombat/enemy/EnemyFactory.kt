package galacticCombat.enemy

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.ENEMY_ID
import galacticCombat.EntityType
import javafx.geometry.Point2D

@Suppress("unused")
class EnemyFactory : EntityFactory {

  @Spawns(ENEMY_ID)
  fun spawnEnemy(data: SpawnData): Entity =
    entityBuilder().type(EntityType.SHIP)
      .from(data)
      .viewWithBBox("enemies/1.1.gif")
      .with(CollidableComponent(true))
      .with(EnemyComponent())
      .build()
}