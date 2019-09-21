package galacticCombat.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.AssetsConfig
import galacticCombat.EntityType
import galacticCombat.TOWER_ID


class TowerFactory : EntityFactory {

  // TODO add read in from kv file to build TowerDataComponent
  @Suppress("unused")
  @Spawns(TOWER_ID)
  fun spawnTower(data: SpawnData): Entity {
    val towerData = parseTowerData(data)

    return entityBuilder().type(EntityType.TOWER)
        .from(data)
        .view(towerData.texture)
        .with(TowerComponent(towerData))
        .build()
  }


  private fun parseTowerData(data: SpawnData): TowerData {
    //TODO implement parsing
    return TowerData(
        10.0,
        1.0,
        300.0,
        AssetsConfig.getTower("3.3.gif"))
  }
}