package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.FXGL.Companion.getGameWorld
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.configs.AssetConfig
import galacticCombat.entities.EntityType
import galacticCombat.entities.TOWER_SPAWN_ID
import galacticCombat.entities.bullet.BulletData
import galacticCombat.entities.generic.InfoComponent
import galacticCombat.entities.setTypeAdvanced
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
        .view(towerData.texture)
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
      val bulletData = BulletData.create(type, 1)

      val asset = when (type) {
        TowerType.CANNON   -> AssetConfig.getTower("cannon_tower_3.gif")
        TowerType.SPORE    -> AssetConfig.getTower("spore_launcher_3.gif")
        TowerType.TACTICAL -> AssetConfig.getTower("ray_blaster_3.gif")
        TowerType.CRYONIC  -> AssetConfig.getTower("cryonic_emitter_3.gif")
      }

      return TowerData(
          type,
          bulletData,
          asset
      )
    }
  }
}