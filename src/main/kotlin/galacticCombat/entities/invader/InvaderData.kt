package galacticCombat.entities.invader

import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.generic.animation.FrameData
import galacticCombat.level.json.Path

data class InvaderData(
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

    //todo create builder from args
  }
}

enum class Speed(val speed: Double) {
  FAST(100.0),
  NORMAL(60.0),
  SLOW(40.0)
}