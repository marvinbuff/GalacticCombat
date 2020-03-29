package galacticCombat.entities.invader

import galacticCombat.configs.AssetConfig
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.generic.animation.FrameData
import galacticCombat.entities.invader.InvaderType.ACCELERATED
import galacticCombat.entities.invader.InvaderType.BASTION
import galacticCombat.entities.invader.InvaderType.COMMON
import galacticCombat.entities.invader.InvaderType.REINFORCED
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
        return InvaderData(args, asset, scale(health, level), speed, armour, 10)
      }

      val frames = when (args.type) {
        COMMON      -> listOf("common_level_${args.level}.gif")
        REINFORCED  -> (1..3).map { "reinforced_level_${args.level}_frame_$it.gif" }
        ACCELERATED -> (1..3).map { "3.${args.level}$it.gif" }
        BASTION     -> listOf("the_bastion.gif")
      }

      val asset = FrameData.create(frames.map { AssetConfig.getInvader(it) })

      return when (args.type) {
        COMMON      -> getInvaderDataFromDynamic(args.level, asset, Speed.NORMAL, 50.0, 2.0)
        REINFORCED  -> getInvaderDataFromDynamic(args.level, asset, Speed.SLOW, 50.0, 4.0)
        ACCELERATED -> getInvaderDataFromDynamic(args.level, asset, Speed.FAST, 30.0, 0.0)
        BASTION     -> InvaderData(args, asset, 300.0, Speed.SLOW, 6.0, 200)
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