package galacticCombat.invader

data class InvaderData(
    val texture: String,
    val maxHealth: Double,
    val baseSpeed: Speed,
    val armour: Double,
    val xp: Int,
    val bounty: Int = 100,
    val damage: Int = 1
) {
  companion object {
    const val id = "InvaderData"
  }
}

enum class Speed(val speed: Double) {
  FAST(100.0),
  NORMAL(60.0),
  SLOW(40.0)
}