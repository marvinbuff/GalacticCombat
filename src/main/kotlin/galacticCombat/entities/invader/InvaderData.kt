package galacticCombat.entities.invader

import galacticCombat.configs.AssetConfig
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.generic.animation.FrameData
import galacticCombat.level.json.InvaderArgs
import galacticCombat.level.json.Path
import kotlin.math.pow

data class InvaderData(
    val args: InvaderArgs,
    val texture: FrameData,
    val maxHealth: Double,
    val baseSpeed: Speed,
    val armour: Double,
    val xp: Int,
    val bounty: Int = 100,
    val damage: Int = 1,
    val wayPoints: Path = LevelDataVar.get().paths.first()
) {
  companion object {
    const val id = "InvaderData"

    fun createFromArgs(args: InvaderArgs): InvaderData {

      fun getInvaderDataFromDynamic(level: Int, asset: FrameData, speed: Speed, health: Double, armour: Double): InvaderData {
        return InvaderData(args, asset, scale(health, level), speed, scale(armour, level), 10)
      }

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

    private fun scale(value: Double, level: Int): Double {
      val scaling = 1.412
      return value * scaling.pow(level)
    }
  }
}

enum class Speed(val value: Double) {
  FAST(100.0),
  NORMAL(60.0),
  SLOW(40.0)
}