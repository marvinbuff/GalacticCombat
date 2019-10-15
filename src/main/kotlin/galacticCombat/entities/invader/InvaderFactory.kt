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
import kotlin.math.pow

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
      if (data.hasKey(InvaderType.id) && data.hasKey(levelId)) {
        getInvaderData(data.get(InvaderType.id), data.get(levelId))
      } else {
        data.get(InvaderData.id)
      }

  private fun getInvaderData(type: InvaderType, level: Int): InvaderData {
    val asset = AssetConfig.getInvader(
        when (type) {
          InvaderType.COMMON      -> "1.$level.gif"
          InvaderType.REINFORCED  -> "2.${level}1.gif"
          InvaderType.ACCELERATED -> "3.${level}1.gif"
        }
    )

    return when (type) {
      InvaderType.COMMON      -> getInvaderDataFromDynamic(level, asset, Speed.NORMAL, 100.0, 2.0)
      InvaderType.REINFORCED  -> getInvaderDataFromDynamic(level, asset, Speed.SLOW, 100.0, 8.0)
      InvaderType.ACCELERATED -> getInvaderDataFromDynamic(level, asset, Speed.FAST, 80.0, 0.0)
    }
  }

  private fun getInvaderDataFromDynamic(level: Int, asset: String, speed: Speed, health: Double, armour: Double): InvaderData {
    return InvaderData(asset, scale(health, level), speed, scale(armour, level), 10)
  }

  private fun scale(value: Double, level: Int): Double {
    val scaling = 1.412
    return value * scaling.pow(level)
  }


  companion object {
    const val levelId = "level"
  }
}