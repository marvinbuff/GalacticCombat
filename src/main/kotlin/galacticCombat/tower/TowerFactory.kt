package galacticCombat.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.EntityType
import galacticCombat.TOWER_ID

class TowerFactory : EntityFactory {

  // TODO add read in from kv file to build TowerDataComponent
  @Spawns(TOWER_ID)
  fun spawnTower(data: SpawnData): Entity =
    entityBuilder().type(EntityType.TOWER)
      .from(data)
      .view("meteor.png")
      .build()
}