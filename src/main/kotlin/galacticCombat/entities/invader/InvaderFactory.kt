package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.configs.AssetConfig
import galacticCombat.entities.EntityType
import galacticCombat.entities.INVADER_ID
import javafx.geometry.Point2D

@Suppress("unused")
class InvaderFactory : EntityFactory {

  @Spawns(INVADER_ID)
  fun spawnEnemy(data: SpawnData): Entity {
    val position = Point2D(data.x, data.y).subtract(InvaderComponent.center)
    val invaderData = parseInvaderData(data)
    val invader = InvaderComponent(invaderData)
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
        .at(position)
        .viewWithBBox(invaderData.texture)
        .with(invader)
        .with(healthBar)
        .build()
  }

  private fun parseInvaderData(data: SpawnData): InvaderData =
      if (data.hasKey(InvaderType.id)) {
        getInvaderData(data.get(InvaderType.id))
      } else {
        data.get(InvaderData.id)
      }

  private fun getInvaderData(type: InvaderType): InvaderData {
    val asset = AssetConfig.getInvader(
        when (type) {
          InvaderType.COMMON      -> "1.2.gif"
          InvaderType.REINFORCED  -> "2.21.gif"
          InvaderType.ACCELERATED -> "3.21.gif"
        }
    )

    return when (type) {
      InvaderType.COMMON      -> InvaderData(asset, 100.0, Speed.NORMAL, 2.0, 10)
      InvaderType.REINFORCED  -> InvaderData(asset, 100.0, Speed.SLOW, 8.0, 10)
      InvaderType.ACCELERATED -> InvaderData(asset, 80.0, Speed.FAST, 0.0, 10)
    }
  }


}