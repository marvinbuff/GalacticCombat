package galacticCombat.invader

data class InvaderData(
  val maxHealth: Double,
  val baseSpeed: Double,
  val xp: Int,
  val texture: String,
  val damage: Int = 1
) {
  companion object {
    const val id = "InvaderData"
  }
}