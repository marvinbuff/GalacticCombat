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
import galacticCombat.entities.generic.animation.AnimationComponent
import galacticCombat.entities.generic.animation.FrameData
import galacticCombat.level.json.InvaderArgs
import galacticCombat.utils.position
import galacticCombat.utils.toPoint
import kotlin.math.pow

@Suppress("unused")
class InvaderFactory : EntityFactory {

  @Spawns(SPAWN_ID_INVADER)
  fun spawnEnemy(data: SpawnData): Entity {
    require(data.hasKey(ID_INVADER_ARGS))

    val invaderData = getInvaderData(data.get(ID_INVADER_ARGS))
    val invader = InvaderComponent(invaderData)
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
        .atAnchored(InvaderComponent.center, data.position)
        .with(AnimationComponent(invaderData.texture))
        .with(invader)
        .with(healthBar)
        .build()
  }

  private fun getInvaderData(args: InvaderArgs): InvaderData {


    val frames = when (args.type) {
      InvaderType.COMMON      -> listOf("common_level_${args.level}.gif")
      InvaderType.REINFORCED  -> (1..3).map { "reinforced_level_${args.level}_frame_$it.gif" }
      InvaderType.ACCELERATED -> (1..3).map { "3.${args.level}$it.gif" }
    }

    val asset = FrameData.create(frames.map { AssetConfig.getInvader(it) })

    return when (args.type) {
      InvaderType.COMMON      -> getInvaderDataFromDynamic(args.level, asset, Speed.NORMAL, 100.0, 2.0)
      InvaderType.REINFORCED  -> getInvaderDataFromDynamic(args.level, asset, Speed.SLOW, 100.0, 8.0)
      InvaderType.ACCELERATED -> getInvaderDataFromDynamic(args.level, asset, Speed.FAST, 80.0, 0.0)
    }
  }

  private fun getInvaderDataFromDynamic(level: Int, asset: FrameData, speed: Speed, health: Double, armour: Double): InvaderData {
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