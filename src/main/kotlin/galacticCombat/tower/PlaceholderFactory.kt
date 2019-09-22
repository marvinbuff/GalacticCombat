package galacticCombat.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.EntityType
import galacticCombat.PLACEHOLDER_SPAWN_ID
import javafx.geometry.Point2D

@Suppress("unused")
class PlaceholderFactory : EntityFactory {

  @Spawns(PLACEHOLDER_SPAWN_ID)
  fun spawnBullet(data: SpawnData): Entity {
    val position = Point2D(data.x, data.y).subtract(TowerComponent.center)
    val towerData: TowerData = data.get(TowerData.id)

    return entityBuilder().type(EntityType.TOWER)
        .view(towerData.texture)
        .at(position)
        .with(PlaceholderComponent(towerData))
        .build()
  }


}