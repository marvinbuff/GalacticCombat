package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.configs.AssetConfig
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.EntityType
import galacticCombat.level.json.InvaderArgs
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D
import kotlin.math.pow

@Suppress("unused")
class InvaderFactory : EntityFactory {

  @Spawns(SPAWN_ID_INVADER)
  fun spawnEnemy(data: SpawnData): Entity {
    require(data.hasKey(ID_INVADER_ARGS))

    val position = Point2D(data.x, data.y).subtract(InvaderComponent.center)
    val invaderData = getInvaderData(data.get(ID_INVADER_ARGS))
    val invader = InvaderComponent(invaderData)
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
        .at(position)
        .viewWithBBox(invaderData.texture)
        .with(invader)
        .with(healthBar)
        .build()
  }

  private fun getInvaderData(args: InvaderArgs): InvaderData {
    val asset = AssetConfig.getInvader(
        when (args.type) {
          InvaderType.COMMON      -> "1.${args.level}.gif"
          InvaderType.REINFORCED  -> "2.${args.level}1.gif"
          InvaderType.ACCELERATED -> "3.${args.level}1.gif"
        }
    )

    return when (args.type) {
      InvaderType.COMMON      -> getInvaderDataFromDynamic(args.level, asset, Speed.NORMAL, 100.0, 2.0)
      InvaderType.REINFORCED  -> getInvaderDataFromDynamic(args.level, asset, Speed.SLOW, 100.0, 8.0)
      InvaderType.ACCELERATED -> getInvaderDataFromDynamic(args.level, asset, Speed.FAST, 80.0, 0.0)
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
    const val ID_LEVEL = "level"
    const val ID_INVADER_ARGS = "InvaderArgs"
    const val SPAWN_ID_INVADER = "Invader"

    fun spawn(args: InvaderArgs) {
      val position = LevelDataVar.get().getPathById(args.pathId).wayPoints.first()
      val data = SpawnData(position.toPoint()).put(ID_INVADER_ARGS, args)

      getGameWorld().spawn(SPAWN_ID_INVADER, data)
    }
  }
}