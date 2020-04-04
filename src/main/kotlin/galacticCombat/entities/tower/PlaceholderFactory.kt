package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.EntityType
import galacticCombat.entities.PLACEHOLDER_SPAWN_ID
import galacticCombat.entities.generic.RangeIndicatorComponent
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.utils.position

@Suppress("unused")
class PlaceholderFactory : EntityFactory {

  @Spawns(PLACEHOLDER_SPAWN_ID)
  fun spawnPlaceholder(data: SpawnData): Entity {
    val towerData: TowerData = data.get(TowerData.id)
    val rangeIndicator = RangeIndicatorComponent(towerData.bulletData.range, TowerComponent.center)

    return entityBuilder().setTypeAdvanced(EntityType.TOWER_PLACEHOLDER)
      .view(towerData.texture)
      .atAnchored(TowerComponent.center, data.position)
      .with(rangeIndicator)
      .with(PlaceholderComponent(towerData))
      .build()
  }

}