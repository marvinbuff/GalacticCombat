package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.FXGL.Companion.getGameWorld
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.configs.TowerConfigVar
import galacticCombat.entities.EntityType
import galacticCombat.entities.TOWER_SPAWN_ID
import galacticCombat.entities.generic.InfoComponent
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.moddable.towerConfig.TowerData
import galacticCombat.moddable.towerConfig.getFirstTexture
import galacticCombat.utils.position
import javafx.geometry.Point2D


class TowerFactory : EntityFactory {

  /**
   * Spawns a tower.
   * SpawnData [data] must contain one of the following entries:
   *  - [TowerType.id]
   *  - [TowerData.id]
   */
  @Suppress("unused")
  @Spawns(TOWER_SPAWN_ID)
  fun spawnTower(data: SpawnData): Entity {
    val towerData = parseTowerData(data)

    // Components
    val projectile = ProjectileComponent(Point2D(0.0, 0.0), 0.1)
    val towerComponent = TowerComponent(towerData)
    val infoHandler = InfoComponent(towerComponent)

    return entityBuilder().setTypeAdvanced(EntityType.TOWER)
      .atAnchored(TowerComponent.center, data.position)
      .rotationOrigin(TowerComponent.center)
      .view(towerData.getFirstTexture())
      .with(projectile)
      .with(towerComponent)
      .with(infoHandler)
      .build()
  }

  private fun parseTowerData(data: SpawnData): TowerData =
    if (data.hasKey(TowerType.id)) {
      getTowerData(data.get(TowerType.id))
    } else {
      data.get(TowerData.id)
    }

  companion object {
    fun spawnFromTowerData(data: TowerData, position: Point2D) {
      getGameWorld().spawn(
        TOWER_SPAWN_ID,
        SpawnData(position).put(TowerData.id, data)
      )
    }

    fun getTowerData(type: TowerType): TowerData {
      val data = TowerConfigVar.get().towerConfigs[type.title]
      check(data != null) { "Could create TowerData for key ${type.title}, possible values are ${TowerConfigVar.get().towerConfigs.keys}." }
      return data
    }
  }
}