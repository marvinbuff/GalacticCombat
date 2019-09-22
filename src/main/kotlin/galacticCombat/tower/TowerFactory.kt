package galacticCombat.tower

import com.almasb.fxgl.dsl.FXGL.Companion.getGameWorld
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.AssetsConfig
import galacticCombat.EntityType
import galacticCombat.TOWER_SPAWN_ID
import galacticCombat.bullet.BulletData
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
    val position = Point2D(data.x, data.y).subtract(TowerComponent.center)
    val towerData = parseTowerData(data)

    return entityBuilder().type(EntityType.TOWER)
        .at(position)
        .view(towerData.texture)
        .with(TowerComponent(towerData))
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
        TowerType.CANNON   -> AssetsConfig.getTower("1.3.gif")
        TowerType.SPORE    -> AssetsConfig.getTower("3.3.gif")
        TowerType.TACTICAL -> AssetsConfig.getTower("4.3.gif")
        TowerType.CRYONIC  -> AssetsConfig.getTower("2.3.gif")
        TowerType.STORM    -> AssetsConfig.getTower("6.3.gif")
      }

      return TowerData(
          type,
          bulletData,
          asset
      )
    }
  }
}

enum class TowerType(val title: String) {
  CANNON("Cannon Tower"),
  SPORE("Spore Launcher"),
  TACTICAL("Tactical Tower"),
  CRYONIC("Cryonic Tower"),
  STORM("Storm Conjurer");

  companion object {
    const val id = "TowerType"
  }
}