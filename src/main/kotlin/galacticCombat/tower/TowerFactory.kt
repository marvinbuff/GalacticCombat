package galacticCombat.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.EntityType

class TowerFactory : EntityFactory {

  @Spawns("Tower")
  fun spawnTower(data: SpawnData): Entity =
    entityBuilder().type(EntityType.TOWER).from(data).view("meteor.png").build()
}