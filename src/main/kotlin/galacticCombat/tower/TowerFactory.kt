package galacticCombat.tower

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.AssetsConfig
import galacticCombat.EntityType
import galacticCombat.TOWER_ID
import galacticCombat.bullet.BulletData
import galacticCombat.bullet.BulletEffect


class TowerFactory : EntityFactory {

  /**
   * Spawns a tower.
   * SpawnData [data] must contain the following entries:
   *  - [TowerType.id]
   */
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
    val type: TowerType = data.get(TowerType.id)

    val bulletData = BulletData.create(type, 1)

    val asset = when(type){
      TowerType.CANNON -> AssetsConfig.getTower("1.3.gif")
      TowerType.SPORE  -> AssetsConfig.getTower("3.3.gif")
      TowerType.TACTICAL -> AssetsConfig.getTower("4.3.gif")
      TowerType.CRYONIC -> AssetsConfig.getTower("2.3.gif")
      TowerType.STORM -> AssetsConfig.getTower("6.3.gif")
    }

    return TowerData(
        bulletData,
        asset)
  }
}

enum class TowerType(val title: String){
  CANNON("Cannon Tower"),
  SPORE("Spore Launcher"),
  TACTICAL("Tactical Tower"),
  CRYONIC("Cryonic Tower"),
  STORM("Storm Conjurer");

  companion object{
    const val id = "TowerType"
  }
}